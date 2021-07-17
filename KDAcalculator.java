import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

class Mapper{
	
	public HashMap<String, Integer> map = new HashMap<>();
	
	public String dir = "";
	
	public void Save(){
		
		String path = System.getProperty("user.dir");
		FileWriter fout = null;
		
		try{
			fout = new FileWriter(path+"\\"+dir);
			
			String str;
			
			str = new String("K" + "," + map.get("K"));
			str = str.trim();
			fout.write(str);
			fout.write("\r\n");
			
			str = new String("D" + "," + map.get("D"));
			str = str.trim();
			fout.write(str);
			fout.write("\r\n");
			
			fout.close();
			
		} catch(IOException e){
			System.out.println("error: IOException e, on Mapper, Save()");
		}
	}
	
	public void Load(){
		String path = System.getProperty("user.dir");
		
		try{
			File file = new File(path+"\\"+dir);
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			
			String line;
			String[] str = new String[2];
            while((line = bufReader.readLine()) != null){
                str = line.split(",");
				this.Add(str[0], Integer.parseInt(str[1]));
            }
			
		} catch(IOException e){
			System.out.print("error: IOException e, on Mapper, Load()");
		}
	}
	
	public void Add(String key, int value){
		map.put(key, value);
	}
	
	public boolean Exist(String key){
		if (map.get(key) != null) return true;
		else return false;
	}
	
	public int GetKill(){
		return map.get("K");
	}
	
	public int GetDeath(){
		return map.get("D");
	}
	
	public Mapper(String dir){
		this.dir = dir;
	}
	
	public void print(){
		for(String str : map.keySet()){
			System.out.println("[Key]:" + str + " [Value]:" + map.get(str));
		}
	}
}



class Data{
	private int data;
	
	public void dataPlus(){
		this.data++;
	}
	
	public void dataMinus(){
		if (this.data > 0)
			this.data--;
	}
	
	public void dataChange(int data){
		if (data >= 0)
			this.data = data;
	}
	
	public int returnData(){
		return data;
	}
	
	public Data(){}
}



class KP extends JPanel{
	public Data DB = new Data();
	private JLabel la = new JLabel("Kills");
	private JTextField tf = new JTextField(14);
	private KDP KDA;
	
	public KP(int data, KDP KDA){
		this.setBackground(Color.LIGHT_GRAY);
		this.add(la);
		tf.setEditable(false);
		this.add(tf);
		
		this.DB.dataChange(data);
		this.KDA = KDA;
		
		JButton plusBTN = new JButton("+");
		plusBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DB.dataPlus();
				resetText();
			}
		});
		
		JButton minusBTN = new JButton("-");
		minusBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DB.dataMinus();
				resetText();
			}
		});
		
		this.add(plusBTN);
		this.add(minusBTN);
		
		resetText();
	}
	
	public void dataChange(int data){
		this.DB.dataChange(data);
	}
	
	public void resetText(){
		this.tf.setText(Integer.toString(DB.returnData()));
		this.KDA.setKill((float)DB.returnData());
	}
}



class DP extends JPanel{
	public Data DB = new Data();
	private JLabel la = new JLabel("Deaths");
	private JTextField tf = new JTextField(15);
	private KDP KDA;
	
	public DP(int data, KDP KDA){
		this.setBackground(Color.LIGHT_GRAY);
		this.add(la);
		tf.setEditable(false);
		this.add(tf);
		
		this.DB.dataChange(data);
		this.KDA = KDA;
		
		JButton plusBTN = new JButton("+");
		plusBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DB.dataPlus();
				resetText();
			}
		});
		
		JButton minusBTN = new JButton("-");
		minusBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DB.dataMinus();
				resetText();
			}
		});
		
		this.add(plusBTN);
		this.add(minusBTN);
		
		resetText();
	}
	
	public void dataChange(int data){
		this.DB.dataChange(data);
	}
	
	public void resetText(){
		this.tf.setText(Integer.toString(DB.returnData()));
		this.KDA.setDeath((float)DB.returnData());
	}
}



class KDP extends JPanel{
	private JLabel la = new JLabel("K/D");
	private JTextField tf = new JTextField(21);
	private float kills = 0f;
	private float deaths = 0f;
	private float result = 0f;
	
	public KDP(){
		this.setBackground(Color.LIGHT_GRAY);
		this.add(la);
		tf.setEditable(false);
		this.add(tf);
		resetText();
	}
	
	public void setKill(float kills){
		this.kills = kills;
		this.resetText();
	}
	
	public void setDeath(float deaths){
		this.deaths = deaths;
		this.resetText();
	}
	
	public void resetText(){
		if (deaths > 0)
			result = kills/deaths;
		this.tf.setText(String.format("%.10f" , result));
	}
}



class UtilP extends JPanel{
	private JTextField tf = new JTextField(10);
	private KP K;
	private DP D;
	
	public UtilP(KP K, DP D){
		Mapper Map = new Mapper("KDAdata.csv");
		Map.Load();
		
		Map.print();
		
		K.DB.dataChange(Map.GetKill());
		K.resetText();
		
		D.DB.dataChange(Map.GetDeath());
		D.resetText();
		
		this.setBackground(Color.LIGHT_GRAY);
		
		JButton saveBTN = new JButton("Save");
		saveBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Map.Add("K",K.DB.returnData());
				Map.Add("D",D.DB.returnData());
				Map.Save();
			}
		});
		this.add(saveBTN);
		
		this.add(tf);
		
		JButton killBTN = new JButton("Kill");
		killBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				K.DB.dataChange(Integer.parseInt(tf.getText()));
				K.resetText();
			}
		});
		
		JButton deathBTN = new JButton("Death");
		deathBTN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				D.DB.dataChange(Integer.parseInt(tf.getText()));
				D.resetText();
			}
		});
		
		this.add(killBTN);
		this.add(deathBTN);
	}
}



public class KDAcalculator extends JFrame{
	public KDAcalculator() {
		setTitle("K/D Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = getContentPane();
		con.setLayout(new GridLayout(4,1,5,5));
		
		KDP KD = new KDP();
		KP K = new KP(0, KD);
		DP D = new DP(0, KD);
		UtilP U = new UtilP(K, D);
		
		con.add(K);
		con.add(D);
		con.add(KD);
		con.add(U);
		
		setSize(360,200);
		setVisible(true);
	}
	
	public static void main(String [] args) {
		new KDAcalculator();
	}
}