package Maze1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import Maze.MazeException;
import Maze.Step;
import 栈.LinkedStack;

import java.io.*;

public class Maze1 extends JFrame implements ActionListener,Runnable
{
	private JButton btn[]=new JButton[5];
	private String str[]= {"生成路径","开始","暂停","继续","结束"};
    private Label label2=new Label("路径：");
    protected JTextArea text=new JTextArea(4,50);
	private JPanel[][] p;
	protected int[][] maze;
	protected int [][] direction= {{0,1},{0,-1},{-1,0},{1,0}};       //方向
	protected Step step=new Step(0,0,0);           //起点
	protected LinkedStack<Step> stack=new LinkedStack<Step>();
	public Thread thread;
	public static String qqq,ppp;
	public static int x,y;
	public static int w;
	public static int a,b;
	public int cc=1;
	public JLabel jlab;
	public JLabel lab=new JLabel("请输入迷宫大小：");
	public JTextField row=new JTextField(4);
	public JTextField column=new JTextField(4);
	public JButton jbutton=new JButton("生成迷宫");
	
	public Maze1()
	{
		super("走迷宫");
		this.setBounds(300,50,700,800);
		this.setBackground(Color.lightGray);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Panel p1=new Panel();
		p1.add(lab);
		p1.add(row);
		p1.add(column);
		jbutton.addActionListener(this);
		p1.add(jbutton);
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
	public Maze1(int[][] maze)
	{
		super("走迷宫");
		this.setBounds(300,50,700,800);
		this.setBackground(Color.lightGray);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Panel p1=new Panel();
		p1.add(lab);
		row.setText(String.valueOf(a));
		column.setText(String.valueOf(b));
		p1.add(row);
		p1.add(column);
		jbutton.addActionListener(this);
		p1.add(jbutton);
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
		for(int i=0;i<maze.length;i++)
			for(int j=0;j<maze[0].length;j++)
			{
				p[i][j]=new JPanel(new BorderLayout());
				p[i][j].setBorder(new LineBorder(new Color(0,0,0)));
				if(maze[i][j]==1)
					p[i][j].setBackground(Color.black);
				p2.add(p[i][j]);
			}
		p[0][0].setBackground(Color.red);
		p[p.length-1][p[0].length-1].setBackground(Color.red);
		this.add(p2, BorderLayout.CENTER);
		JPanel p3=new JPanel();
		p3.add(label2);
		text.setLineWrap(true);
		p3.add(text);
		this.add(p3, BorderLayout.SOUTH);
		this.maze=new int[maze.length][maze[0].length];
		for(int i=0;i<maze.length;i++)
			for(int j=0;j<maze[0].length;j++)
				this.maze[i][j]=maze[i][j];
	}
	public int[][] setMaze(int i,int j)
	{
		int[][] maze=new int[i][j];
		for(int m=0;m<maze.length;m++)
			for(int n=0;n<maze[0].length;n++)
				maze[m][n]=Math.random()>0.5?1:0;
		maze[0][0]=0;
		maze[i-1][j-1]=0;
		return maze;
	}
	public void actionPerformed(ActionEvent ev)
	{
		if(ev.getActionCommand().equals("生成迷宫"))
		{
			try
			{
				a=Integer.parseInt(row.getText());
				b=Integer.parseInt(column.getText());
			}
			catch(NumberFormatException e) 
			{
				JOptionPane.showMessageDialog(null, "请输入整数！", "提示", 0);
				a=b=5;
			}
			this.setVisible(false);
			Maze1 m=new Maze1(this.setMaze(a,b));
			m.setVisible(true);
		}
		if(ev.getActionCommand().equals("生成路径"))
		{
			qqq=this.findpath(this.maze);
			ppp=qqq;
			this.text.setText(qqq);
		}
		if(ev.getActionCommand().equals("开始"))
		{
			this.setVisible(true);
			thread=new Thread(this);
			thread.start();
		}
		if(ev.getActionCommand().equals("暂停"))
		{
			thread.interrupt();
		}
		if(ev.getActionCommand().equals("继续"))
		{
			text.setText(ppp);
			this.setVisible(true);
			if(w+2<qqq.length())
				qqq=qqq.substring(w+2);
			thread=new Thread(this);
			thread.start();
		}
		if(ev.getActionCommand().equals("结束"))
		{
			thread.stop();
		}
	}
	public String findpath(int[][] maze)          //用栈实现迷宫算法
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
			    	str+="(*"+step.x+","+step.y+")";
					stack.pop();
					if(!stack.isEmpty())
						step=stack.peek();
			//        str+="("+step.x+","+step.y+")";
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
		Maze1 maze=new Maze1();
		maze.setVisible(true);
	}
	public void run()
	{
		int m=0,n=0;
		int c=cc;
		int i=0;
		try
		{
			for(;i<qqq.length();i++)
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
							if(sss.charAt(1)=='*')
							{
								m=Integer.parseInt(sss.substring(2,k));
								n=Integer.parseInt(sss.substring(k+1));
								p[m][n].setBackground(Color.pink);
								p[m][n].removeAll();
								jlab=new JLabel(String.valueOf(c++));
								jlab.setFont(new java.awt.Font("宋体",Font.BOLD,50));
								p[m][n].add(jlab);
								validate();
								Thread.sleep(500);
							}
							else
							{
								m=Integer.parseInt(sss.substring(1,k));
								n=Integer.parseInt(sss.substring(k+1));
								p[m][n].removeAll();
								jlab=new JLabel(String.valueOf(c++));
								jlab.setFont(new java.awt.Font("宋体",Font.BOLD,50));
								p[m][n].add(jlab);
								validate();
								Thread.sleep(500);
							}
						}
					}
				}
			}
		}
		catch(InterruptedException ex)
		{
			   // 	ex.printStackTrace();
			 x=m;
			 y=n;
			 w=i+2;
			 cc=c;
		}
		if(m!=maze.length-1||n!=maze[0].length-1)
			JOptionPane.showMessageDialog(null, "迷宫没有走通！", "提示", 0); 
		if(m==maze.length-1&&n==maze[0].length-1)
			JOptionPane.showMessageDialog(null, "迷宫成功走通！", "提示", 1); 
	}
}
