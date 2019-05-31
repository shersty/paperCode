package ExcelTest2.standard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import cesudu.staticResultMine;

public class TEST {

	@Test
	public void tt(){
		
		List<Integer> l1 = new ArrayList<>();
		List<Integer> l2 = new ArrayList<>();
		l1.add(1);
		l1.add(2);
//		l2.add(1);
//		l2.add(2);
		l2.addAll(l1);
		System.out.println(l1.equals(l2));
//		String f = "9.8e-05";
//		System.out.println(Double.parseDouble(f));
//		Set<Integer> set = new HashSet<>();
//		
//		set.add(4);
//		set.add(5);
//		System.out.println(set.size());
//		set.add(5);
//		
//		System.out.println(set.size());
//		System.out.println(set.toString());
	}

    public static void buildStandardList(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));

        String content = "";
        while(content!=null){
            content = br.readLine();
            if(content ==null )break;
            System.out.println(content.trim());
        }

        br.close();

    }

    public static void main(String[] args) throws IOException {

        buildStandardList(new File("data\\continousAdult.txt"));
    }
}
