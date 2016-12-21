package testIdea;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable{
	
	private Selector selector;
	
	private ServerSocketChannel servChannel;	
	
	private volatile boolean stop;
	/**
	 * 初始化多路复用器、绑定监听端口
	 */
	public MultiplexerTimeServer(int port){
		try{
			selector = Selector.open();
			servChannel = ServerSocketChannel.open();
			servChannel.configureBlocking(false);
			servChannel.socket().bind(new InetSocketAddress(port),1024);
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The time server is start in port: " + port);
			
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop(){
		this.stop = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop){
			try{
				//每隔1s被唤醒一次
				selector.select(1000);
				//返回其中就绪状态的selectionkey集合
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it =  selectedKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try{
						handleInput(key);
					}catch(Exception e){
						if(key != null){
							key.cancel();
							if(key.channel() != null)
								key.channel().close();
						}
					}
				}
				
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
		if(selector != null)
			try{
				selector.close();
			}catch(IOException e){
				e.printStackTrace();
			}
	}
	/**
	 * 处理新接入的客户端请求信息
	 * 根据SelectionKey的操作位进行判断
	 * @param key
	 * @throws IOException
	 */
	private void handleInput(SelectionKey key) throws IOException{
		if (key.isValid()){
			
			if(key.isAcceptable()){
				//accept the new connection
				ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				//add the new connection to the selector
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				//read the data
				SocketChannel sc = (SocketChannel)key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes > 0){
					//flip操作的作用是将缓冲区当前的limit设置为position，设为0，用于后续对缓冲区的读取操作
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes,"UTF-8");
					System.out.println("The time server receive order: " + body);
					String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
							?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
					doWrite(sc,currentTime);		
				}else if(readBytes < 0){
					key.cancel();
					sc.close();
				}else
					;
				
			}
		}
	}
	/**
	 * 将应答消息异步发送到客户端
	 * @param channel
	 * @param response
	 * @throws IOException
	 */
	private void doWrite(SocketChannel channel,String response) 
		throws IOException{
		if(response != null && response.trim().length() > 0){
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			//flip操作
			writeBuffer.flip();
			channel.write(writeBuffer);
		}
	}
	
}
