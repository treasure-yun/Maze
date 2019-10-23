package Maze;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import Maze.Step;
import 栈.LinkedStack;

import java.io.*;

public class Maze extends JFrame implements ActionListener,Runnable
{
	private JButton btn[]=new JButton[5];
	private String str[]= {"生成路径","开始","暂停","继续","结束"};
	private JComboBox combobox;
	private String s[]= {" ","简单","中等","困难"};
    private Label label1=new Label("选择难度:");
    private Label label2=new Label("路径：");
    protected JTextArea text=new JTextArea(4,50);
	private JPanel[][] p;
	public JLabel[][] jlabel;
	protected int[][] maze;
	protected int [][] direction= {{0,1},{0,-1},{-1,0},{1,0}};       //方向
	protected Step step=new Step(0,0,0);           //起点
	protected LinkedStack<Step> stack=new LinkedStack<Step>();
	private JPanel[] panel=new JPanel[3];
	private File file;
	public Thread thread;
	public static String qqq,ppp;
	public static int x,y;
	public static int w;
	
	public Maze()
	{
		super("走迷宫");
		this.setBounds(300,50,700,800);
		this.setBackground(Color.lightGray);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Panel p1=new Panel();
		p1.add(label1);
		p1.add(combobox=new JComboBox<String>(s));
		combobox.addActionListener(this);
		for(int i=0;i<btn.length;i++)
		{
			btn[i]=new JButton(str[i]);
			p1.add(btn[i]);
			btn[i].addActionListener(this);
		}
		this.add(p1,BorderLayout.NORTH);
		Panel p2=new Panel();
		this.add(p2, BorderLayout.CENTER);
		JPanel p3=new JPanel();
		p3.add(label2);
		text.setLineWrap(true);
		p3.add(text);
		this.add(p3, BorderLayout.SOUTH);
	}
	public Maze(int[][] maze,String filename,int k)
	{
		super("走迷宫");
		this.setBounds(300,50,700,800);
		this.setBackground(Color.lightGray);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Panel p1=new Panel();
		p1.add(label1);
		combobox=new JComboBox<String>(s);
		combobox.setSelectedIndex(k);
		p1.add(combobox);
		combobox.addActionListener(this);
		for(int i=0;i<btn.length;i++)
		{
			btn[i]=new JButton(str[i]);
			p1.add(btn[i]);
			btn[i].addActionListener(this);
		}
		this.add(p1,BorderLayout.NORTH);
		Panel p2=new Panel();
		p2.setLayout(new GridLayout(maze.length,maze[0].length));
		p=new JPanel[maze.length][maze[0].length];
		jlabel=new JLabel[maze.length][maze[0].length];
		ImageIcon icon=new ImageIcon(filename);
		for(int i=0;i<maze.length;i++)
			for(int j=0;j<maze[0].length;j++)
			{
				p[i][j]=new JPanel(new BorderLayout());
				p[i][j].setBorder(new LineBorder(new Color(0,0,0)));
				if(maze[i][j]==1)
					p[i][j].setBackground(Color.black);
				jlabel[i][j]=new JLabel();
				jlabel[i][j].setIcon(icon);
				jlabel[i][j].setLocation(p[i][j].getX(),p[i][j].getY());
				jlabel[i][j].setVisible(false);
				p[i][j].add(jlabel[i][j]);
				p2.add(p[i][j]);
			}
		jlabel[0][0].setVisible(true);
		p[0][0].setBackground(Color.red);
		p[p.length-1][p[0].length-1].setBackground(Color.red);
		this.add(p2, BorderLayout.CENTER);
		JPanel p3=new JPanel();
		p3.add(label2);
		text.setLineWrap(true);
		p3.add(text);
		this.add(p3, BorderLayout.SOUTH);
	}
	public int[][] readFrom(String filename,int x)       //字节流
	{
		try
		{
			FileReader fr=new FileReader(filename);
			BufferedReader dr=new BufferedReader(fr);
			String str;
			if(x==1)
			{
				maze=new int[5][5];
				int n=0;    //行数
				while((str=dr.readLine())!=null)
				{
					for(int m=0;m<5;m++)
					{
						try
						{
							if(str.charAt(m)=='0'||str.charAt(m)=='1')
								maze[n][m]=(int)str.charAt(m)-48;
							else
								throw new MazeException();
						}
						catch(MazeException e){JOptionPane.showMessageDialog(null, "请输入01字符！", "提示", 0);}
					}
					n++;
				}
			}
			if(x==2)
			{
				maze=new int[10][10];
				int n=0;    //行数
				while((str=dr.readLine())!=null)
				{
					for(int m=0;m<10;m++)
					{
						try
						{
							if(str.charAt(m)=='0'||str.charAt(m)=='1')
								maze[n][m]=(int)str.charAt(m)-48;
							else
								throw new MazeException();
						}
						catch(MazeException e){JOptionPane.showMessageDialog(null, "请输入01字符！", "提示", 0);}
					}
					n++;
				}
			}
			if(x==3)
			{
				maze=new int[15][15];
				int n=0;    //行数
				while((str=dr.readLine())!=null)
				{
					for(int m=0;m<15;m++)
					{
						try
						{
							if(str.charAt(m)=='0'||str.charAt(m)=='1')
								maze[n][m]=(int)str.charAt(m)-48;
							else
								throw new MazeException();
						}
						catch(MazeException e){JOptionPane.showMessageDialog(null, "请输入01字符！", "提示", 0);}
					}
					n++;
				}
			}
			dr.close();
			fr.close();
		}
		catch(IOException e) {}
		return maze;
	}
	public void actionPerformed(ActionEvent ev)
	{
		if(this.combobox.getSelectedIndex()==0)
		{
			this.setVisible(false);
			Maze m=new Maze();
			m.setVisible(true);
		}
		if(this.combobox.getSelectedIndex()==1)
		{
			this.setVisible(false);
			maze=readFrom("D:\\myjava\\迷宫\\迷宫地图\\简单.txt",1);
			Maze m=new Maze(maze,"D:\\myjava\\迷宫\\人1.png",1);
			m.setVisible(true);
			if(ev.getActionCommand().equals("生成路径"))
			{
				m.setVisible(false);
				this.setVisible(true);
				qqq=m.findpath(maze);
				ppp=qqq;
				this.text.setText(qqq);
			}
			if(ev.getActionCommand().equals("开始"))
			{
				m.setVisible(false);
				this.setVisible(true);
				thread=new Thread(this);
				thread.start();
			}
			if(ev.getActionCommand().equals("暂停"))
			{
				thread.interrupt();
				m.findpath(maze);
				this.setVisible(false);
				m.setVisible(true);
				m.jlabel[0][0].setVisible(false);
				m.jlabel[x][y].setVisible(true);
			}
			if(ev.getActionCommand().equals("继续"))
			{
				text.setText(ppp);
				m.setVisible(false);
				this.setVisible(true);
				this.jlabel[x][y].setVisible(false);
				if(w+2<qqq.length())
					qqq=qqq.substring(w+2);
				thread=new Thread(this);
				thread.start();
			}
			if(ev.getActionCommand().equals("结束"))
				thread.interrupt();
		}
		if(this.combobox.getSelectedIndex()==2)
		{
			this.setVisible(false);
			maze=readFrom("D:\\myjava\\迷宫\\迷宫地图\\中等.txt",2);
			Maze m=new Maze(maze,"D:\\myjava\\迷宫\\人2.png",2);
			m.setVisible(true);
			if(ev.getActionCommand().equals("生成路径"))
			{
				m.setVisible(false);
				this.setVisible(true);
				qqq=m.findpath(maze);
				ppp=qqq;
				this.text.setText(qqq);
			}
			if(ev.getActionCommand().equals("开始"))
			{
				m.setVisible(false);
				this.setVisible(true);
				thread=new Thread(this);
				thread.start();
			}
			if(ev.getActionCommand().equals("暂停"))
			{
				thread.interrupt();
				m.findpath(maze);
				this.setVisible(false);
				m.setVisible(true);
				m.jlabel[0][0].setVisible(false);
				m.jlabel[x][y].setVisible(true);
			}
			if(ev.getActionCommand().equals("继续"))
			{
				text.setText(ppp);
				m.setVisible(false);
				this.setVisible(true);
				this.jlabel[x][y].setVisible(false);
				if(w+2<qqq.length())
					qqq=qqq.substring(w+2);
				thread=new Thread(this);
				thread.start();
			}
			if(ev.getActionCommand().equals("结束"))
				thread.interrupt();
		}
		if(this.combobox.getSelectedIndex()==3)
		{
			this.setVisible(false);
			maze=readFrom("D:\\myjava\\迷宫\\迷宫地图\\困难.txt",3);
			Maze m=new Maze(maze,"D:\\myjava\\迷宫\\人3.png",3);
			m.setVisible(true);
			if(ev.getActionCommand().equals("生成路径"))
			{
				m.setVisible(false);
				this.setVisible(true);
				qqq=m.findpath(maze);
				ppp=qqq;
				this.text.setText(qqq);
			}
			if(ev.getActionCommand().equals("开始"))
			{
				m.setVisible(false);
				this.setVisible(true);
				thread=new Thread(this);
				thread.start();
			}
			if(ev.getActionCommand().equals("暂停"))
			{
				thread.interrupt();
				m.findpath(maze);
				this.setVisible(false);
				m.setVisible(true);
				m.jlabel[0][0].setVisible(false);
				m.jlabel[x][y].setVisible(true);
			}
			if(ev.getActionCommand().equals("继续"))
			{
				text.setText(ppp);
				m.setVisible(false);
				this.setVisible(true);
				this.jlabel[x][y].setVisible(false);
				if(w+2<qqq.length())
					qqq=qqq.substring(w+2);
				thread=new Thread(this);
				thread.start();
			}
			if(ev.getActionCommand().equals("结束"))
				thread.interrupt();
		}
	}
	public String findpath(int[][] maze)
	{  
		String str="";
		int i=step.x;
        int j=step.y;
		while(i>=0&&j>=0&&i<maze.length&&j<maze[0].length&&maze[i][j]==0)
		{
			while(i>=0&&j>=0&&i<maze.length&&j<maze[0].length&&maze[i][j]==0)
		    {
			    step.x=i;
		     	step.y=j;
		     	str+="("+step.x+","+step.y+")";
				maze[step.x][step.y]=-1;
//				stack.push(step);
				stack.push(new Step(step));
				if(i==maze.length-1&&j==maze[0].length-1)
				{
				//	System.out.println("成功走到终点！");
					return str;
				}
				if(step.x+direction[step.f%4][0]>=0&&step.y+direction[step.f%4][1]>=0&&step.x+direction[step.f%4][0]<maze.length&&step.y+direction[step.f%4][1]<maze[0].length)
		        {
					i=step.x+direction[step.f%4][0];
					j=step.y+direction[step.f%4][1];
		        }
	     	}
			int k=1;
			while(i>=0&&j>=0&&i<maze.length&&j<maze[0].length&&maze[i][j]!=0&&k<4)
			{
				
				step.f=step.f+k;
				k++;
				if(step.x+direction[step.f%4][0]>=0&&step.y+direction[step.f%4][1]>=0&&step.x+direction[step.f%4][0]<maze.length&&step.y+direction[step.f%4][1]<maze[0].length)
		        {
					i=step.x+direction[step.f%4][0];
					j=step.y+direction[step.f%4][1];
		        }
			    while(i>=0&&j>=0&&i<maze.length&&j<maze[0].length&&maze[i][j]!=0&&!stack.isEmpty()&&k==4)
				{
					stack.pop();
				//	System.out.println("("+step.x+","+step.y+")");
					if(!stack.isEmpty())
						step=stack.peek();
			        str+="("+step.x+","+step.y+")";
			        if(step.x+direction[step.f%4][0]>=0&&step.y+direction[step.f%4][1]>=0&&step.x+direction[step.f%4][0]<maze.length&&step.y+direction[step.f%4][1]<maze[0].length)
			        {
						i=step.x+direction[step.f%4][0];
						j=step.y+direction[step.f%4][1];
			        }
					k=1;
				}
			}
			text.setText(str);
		}
		return str;
	}
	public static void main(String[] args)
	{
		Maze maze=new Maze();
		maze.setVisible(true);
	}
	public void run()
	{
		int m=0,n=0;
		for(int i=0;i<qqq.length();i++)
		{
			if(qqq.charAt(i)=='(')
			{
				int j=i;
				while(qqq.charAt(j)!=')')
					j++;
				String sss=qqq.substring(i,j);
				for(int k=0;k<sss.length();k++)
				{
					if(sss.charAt(k)==',')
					{
						m=Integer.parseInt(sss.substring(1,k));
						n=Integer.parseInt(sss.substring(k+1));
						try
						{
							this.jlabel[m][n].setVisible(true);
							Thread.sleep(500);
							this.jlabel[m][n].setVisible(false);
						}
						catch(InterruptedException ex)
						{
					   // 	ex.printStackTrace();
					    	x=m;
					    	y=n;
					    	w=i+2;
					    	break;
					    }
					}
				}
			}
		}
		jlabel[m][n].setVisible(true);
	}
}
