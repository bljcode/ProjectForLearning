package testIdea;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class TestYourIdea
{
    public static void main( String args[] ){
 
    	System.out.println(" the  date : " + getIncrementDate(1));
   
   }
    /**
	 * ���ڻ�õ�ǰ����
	 * @return Date ������ʱ��
	 */
	public static Date getIncrementDate(int incrementDay) {
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,incrementDay) ;
		date = calendar.getTime();
		return date;
	}
}
