package App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.naming.spi.DirStateFactory.Result;


public class Solution {
	private void createTemp(ArrayList<String> ip_addresses, int filenum) {
		// TODO Auto-generated method stub
		String tempFileName = "temp"+filenum+".txt";
			
			FileWriter fw;
			try {
				fw = new FileWriter(tempFileName);
			 
				for (int i = 0; i < ip_addresses.size(); i++) {
					fw.write(ip_addresses.get(i)+"\n");
				}
				fw.close();
			}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
	}
	
	public void mergeFiles(int filenum)
	{
		BufferedReader br[] = new BufferedReader[filenum+1];
		String checker[] = new String[filenum+1];
		IPComparator1 ipc = new IPComparator1();
		FileWriter fw;
		try
		{
			fw = new FileWriter("output.txt");
			for(int i=0;i<=filenum;i++)
			{
				br[i] = new BufferedReader(new FileReader("temp"+i+".txt"));
				checker[i] = br[i].readLine();
			}
			while(true)
			{
				String min = checker[0];
				int j=0;
				for(int i=0;i<=filenum;i++)
				{
					if(ipc.compare(min, checker[i])==1)
					{
						min = checker[i];
						j=i;
					}
				}
				
				checker[j] = br[j].readLine();
				if(checker[j]==null)
				{
					checker[j] = "256.256.256.256";
				}
				if(min.equals("256.256.256.256"))
				{
					break;
				}
				fw.write(min+"\n");
			}
			fw.close();
			for(int i=0;i<=filenum;i++)
			{
				br[i].close();
				File file = new File("temp"+i+".txt");
				file.delete();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void findMaxFive(int filenum)
	{
		int count = 1;
		BufferedReader br;
		String previous = new String();
		PriorityQueue<ResultFormat> maxFive  = new PriorityQueue<ResultFormat>(filenum+1,new ResultComparator());
		try
		{
			br = new BufferedReader(new FileReader("output.txt"));
			String x;
			previous = br.readLine();
			while ( (x = br.readLine()) != null ) 
			{
				if(x.equals(previous))
				{
					count++;
				}
				else
				{
					ResultFormat rf = new ResultFormat(previous,count);
					if(maxFive.size()<5)
					{
						maxFive.add(rf);
					}
					else if(maxFive.element().getCount()<rf.getCount())
					{
						maxFive.remove();
						maxFive.add(rf);
					}
					previous = x;
					count = 1;
				}
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		ResultFormat rf = new ResultFormat(previous,count);
		if(maxFive.size()==0)
		{
			System.out.println("Only 1 user: ");
			System.out.println(rf.getCount() + " " +rf.getIp());
		}
		else
		{
			if(maxFive.element().getCount()<rf.getCount())
			{
				maxFive.remove();
				maxFive.add(rf);
			}
			System.out.println("The max 5 users are as follows:");
		}
		
		while(!maxFive.isEmpty())
		{
			ResultFormat rf1 = new ResultFormat();
			rf = maxFive.remove();
			
			System.out.println(rf.getCount() + " " +rf.getIp());
		}
		
	}
	
	public static void main(String args[])
	{
		ArrayList<String> ip_addresses = new ArrayList<String>();
		BufferedReader br;
		Solution s = new Solution();
		int count = 0;
            int filenum = 0;
            try {
                String x;
                br = new BufferedReader(new FileReader("sample.txt"));
                while ( (x = br.readLine()) != null ) 
                {
                	int first_space = x.indexOf(" ",0);
                	int second_space = x.indexOf(" ", first_space + 1);
                    x = x.substring(first_space+1, second_space);                    
                    ip_addresses.add(x);   
                    count++;
                    if(count%100000 == 0)
                    {
                    	Collections.sort(ip_addresses, new IPComparator1());
                    	s.createTemp(ip_addresses,filenum);
                    	filenum++;
                    	ip_addresses.clear();
                    } 
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            Collections.sort(ip_addresses, new IPComparator1());
        	s.createTemp(ip_addresses,filenum);
        	s.mergeFiles(filenum);
        	s.findMaxFive(filenum);
        	File file = new File("output.txt");
        	file.delete();
	}

	
}

class ResultComparator implements Comparator<ResultFormat>{

	@Override
	public int compare(ResultFormat o1, ResultFormat o2) {
		// TODO Auto-generated method stub
		return o1.getCount()-o2.getCount();
	}
	
}

class IPComparator1  implements Comparator<String> {
	public int compare(String ip1, String ip2) {
		if(ip1.equals(ip2))
			return 0;
		int []octet_values1 = new int[4];
		int []octet_values2 = new int[4];
		setOctetValues(octet_values1, ip1);
		setOctetValues(octet_values2, ip2);		
		for(int i=0; i<4; i++) {
			if(octet_values1[i] < octet_values2[i])
				return -1;
			else if(octet_values1[i] > octet_values2[i])
				return 1;
		}		
		return 1;
	}
	
	public void setOctetValues(int []octet_values, String ip) {
		int first_dot = ip.indexOf(".");
		int second_dot = ip.indexOf(".", first_dot + 1);
		int third_dot = ip.indexOf(".", second_dot + 1);
		octet_values[0] = Integer.parseInt(ip.substring(0, first_dot));
		octet_values[1] = Integer.parseInt(ip.substring(first_dot+1, second_dot));
		octet_values[2] = Integer.parseInt(ip.substring(second_dot+1, third_dot));
		octet_values[3] = Integer.parseInt(ip.substring(third_dot+1, ip.length()));
	}
}

class ResultFormat{
	private String ip;
	private int count;
	ResultFormat(String x, int y){
		ip = x;
		count = y;
		
	}
	public ResultFormat() {
		// TODO Auto-generated constructor stub
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}

