/**
 * 这个计算器在逻辑上和Windows计算器已尽量相像，
 * 充分考虑到用户的任何输入和Windows计算器有相同的行为，
 * 但是代码比较沉长，还有优化的可能。
 * 程序对于处理处理除数为0的异常没有很好的解决，比如
 * 在文本框显示“除数不能为0”之后的计算都会有一些问题，原因
 * 是文本框当前显示的是字符串而非可被解析为数字的字符串。
 * 另外，还没有实现只接受数字和运算符来自键盘的输入。
 * 
 * 最后啊，这个计算器功能上大致是没问题了，但是外观可不敢恭维了。(╯︵╰,)
 */

package com.test.my_sub_caculator_V3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class MyCalculator_V3
{
	private static JFrame frame;
	
	private static JTextField textField;			//文本输入区，位于BorderLayout.NORTH
	
	private static JPanel panButtonLeftField;		//按钮区域，位于BorderLayout.WEST
	
	private static JPanel panButtonRightField;		//按钮区域，位于BorderLayout.EAST
	private static JPanel panButtonUpRightField;	//按钮区域，位于BorderLayout.EAST的UP区域
	private static JPanel panButtonDownRightField;	//按钮区域，位于BorderLayout.EAST的Down区域
	
	/*
	 * 声明每个按钮
	 */
	private static JButton btn_void, btn_MC, btn_MR, btn_MS, btn_Mplus;
	private static JButton btn_Backspace, btn_CE, btn_C;
	private static JButton btn_7, btn_8, btn_9, btn_4, btn_5, btn_6, 
							btn_1, btn_2, btn_3, btn_0, btn_plusOrminus, btn_dot;
	private static JButton btn_div, btn_mul, btn_sub, btn_plus, btn_sqrt, btn_per, btn_reci, btn_eql;
	
	/*
	 * 静态初始化每个按钮
	 */
	static
	{
		btn_void = new JButton();
		btn_MC = new JButton("MC");
		btn_MR = new JButton("MR");
		btn_MS = new JButton("MS");
		btn_Mplus = new JButton("M+");

		btn_Backspace = new JButton("Backspace"); btn_CE = new JButton("CE"); btn_C = new JButton("C");
		
		btn_7 = new JButton("7"); btn_8 = new JButton("8"); btn_9 = new JButton("9");
		btn_4 = new JButton("4"); btn_5 = new JButton("5"); btn_6 = new JButton("6");
		btn_1 = new JButton("1"); btn_2 = new JButton("2"); btn_3 = new JButton("3");
		btn_0 = new JButton("0"); btn_plusOrminus = new JButton("±"); btn_dot = new JButton(".");
		
		btn_mul = new JButton("/");
		btn_sub = new JButton("*");
		btn_div = new JButton("-");
		btn_plus = new JButton("+");
		btn_sqrt = new JButton("sqrt");
		btn_per = new JButton("%");
		btn_reci = new JButton("1/x");
		btn_eql = new JButton("=");
	}
	/*
	 * 文本框的状态有两种，结果和非结果
	 * 如果被判定为结果状态，则通过输入，文本框的内容直接覆盖				
	 * 否则新输入的内容附加到文本框原有文本的后面，具体由numInput()方法实现
	 */
	private static enum TfState
	{
		RESULT, NON_RESULT
	}
	static TfState state = TfState.RESULT;
	
	/*
	 * 初始化变量，总觉得搞这么多静态变量不太妥当啊...
	 * result即结果，用于输出到文本框；preOperand被作为上一个操作数，主要用于
	 * 计算结果；iosOperand是一个用于在输入等号之后存储的独立的运算数；eqlSign
	 * 用于存储是否已输入过等号的标记；numRegister是数字寄存器，存储M键的数字
	 */
	private static final String INIT_NUM = "0";
	static double result = 0;
	static double preOperand = 0;
	static double isoOperand = 0;
	static boolean eqlSign = false;
	static String sign = "";
	
	static double numRegister = 0;
	
	//格式化器，格式化为只保留3位小数，并去掉无用的0
	static DecimalFormat formatter = new DecimalFormat("#.###");
	
	public static void visualizer()
	{
		frame = new JFrame("MyCalculator");
		
		textField = new JTextField();
		
		//M键区域设置位5行1列
		panButtonLeftField = new JPanel();
		panButtonLeftField.setLayout(new GridLayout(5, 1));
		
		//添加按键到M键区域
		btn_void.setEnabled(false);
		panButtonLeftField.add(btn_void);
		panButtonLeftField.add(btn_MC);
		panButtonLeftField.add(btn_MR);
		panButtonLeftField.add(btn_MS);
		panButtonLeftField.add(btn_Mplus);
		
		//M键的右边区域
		panButtonRightField = new JPanel();
		panButtonRightField.setLayout(new BorderLayout());
		
		//M键右边区域的上部分的清零和退格键作为1行3列
		panButtonUpRightField = new JPanel();
		panButtonUpRightField.setLayout(new GridLayout(1, 3));
		
		panButtonUpRightField.add(btn_Backspace);
		panButtonUpRightField.add(btn_CE);
		panButtonUpRightField.add(btn_C);
		
		//数字和运算符区域
		panButtonDownRightField = new JPanel();
		panButtonDownRightField.setLayout(new GridLayout(4, 5));
		panButtonDownRightField.add(btn_7);
		panButtonDownRightField.add(btn_8);
		panButtonDownRightField.add(btn_9);
		panButtonDownRightField.add(btn_mul);
		panButtonDownRightField.add(btn_sqrt);
		panButtonDownRightField.add(btn_4);
		panButtonDownRightField.add(btn_5);
		panButtonDownRightField.add(btn_6);
		panButtonDownRightField.add(btn_sub);
		panButtonDownRightField.add(btn_per);
		panButtonDownRightField.add(btn_1);
		panButtonDownRightField.add(btn_2);
		panButtonDownRightField.add(btn_3);
		panButtonDownRightField.add(btn_div);
		panButtonDownRightField.add(btn_reci);
		panButtonDownRightField.add(btn_0);
		panButtonDownRightField.add(btn_plusOrminus);
		panButtonDownRightField.add(btn_dot);
		panButtonDownRightField.add(btn_plus);
		panButtonDownRightField.add(btn_eql);
		
		//创建数组主要是方便使用循环简单添加键盘监听器
		JButton[] buttons = new JButton[]
				{
						btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, 
						btn_plusOrminus, btn_dot, btn_plus, btn_div, btn_sub, btn_mul, 
						btn_sqrt, btn_per, btn_reci, btn_eql,
						btn_Backspace, btn_CE, btn_C, btn_void, btn_MC, btn_MR, btn_MS, btn_Mplus
				};
		for(JButton button : buttons)
		{
			button.addActionListener(new ButtonListener());
		}
		
		//将M键右侧的区域设置为北（退格和清零键）和南两个区域
		panButtonRightField.add(panButtonUpRightField, BorderLayout.NORTH);
		panButtonRightField.add(panButtonDownRightField, BorderLayout.SOUTH);
		
		//文本区域是整个frame的北，M键区域是frame的西，剩下的放置到东区域
		frame.add(textField, BorderLayout.NORTH);
		frame.add(panButtonLeftField, BorderLayout.WEST);
		frame.add(panButtonRightField, BorderLayout.EAST);
		
		textField.setText(INIT_NUM);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	/*
	 * 所有的按键都集中在同一个按键监听器中，并创建了几个方法
	 * 创建方法的目的都是为了尽可能减少重复的代码，提高可读性
	 */
	public static class ButtonListener implements ActionListener
	{
		public void outputResult(String sign, double preOperand)
		{
			if(sign.equals("/") && preOperand == 0)
			{
				textField.setText("除数不能为0");
				/**
				 * TODO 在此之后应该不能接受任何输入，除C键和CE键之外
				 */
			}
			else
			{
				textField.setText(formatter.format(result));
			}
		}
		public void backspace()
		{
			if(textField.getText().length() > 1)
			{
				textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
			}
			else
			{
				textField.setText("0");
			}
		}
		
		public double operation(String operator)
		{
			if(operator.equals("+"))
			{
				result += preOperand;
			}
			if(operator.equals("-"))
			{
				result -= preOperand;
			}
			if(operator.equals("*"))
			{
				result *= preOperand;
			}
			if(operator.equals("/"))
			{
				result /= preOperand;
			}
			else if(operator.equals(""))
			{
				result = preOperand;
			}
			return result;
		}
		/*
		 * 输入数字和小数点有三个影响因素：1.文本框是否已有小数点；
		 * 2.当前键入的是否是小数点；3.文本框内容是否等于0。
		 * 此外，根据输入前的文本框状态（结果和非结果）也会有所不同。
		 */
		public void numInput(String btnName)
		{
			if(state == TfState.RESULT)
			{
				if(btnName.equals(".")) 
				{
					textField.setText("0.");
				}
				else
					textField.setText(btnName);
			}
			if(state == TfState.NON_RESULT)
			{
				if(!textField.getText().contains("."))
				{
					if(Double.parseDouble(textField.getText()) != 0)
					{
						textField.setText(textField.getText() + btnName);
					}
					else
					{
						if(btnName.equals("."))
						{
							textField.setText(textField.getText() + btnName);
						}
						else
						{
							textField.setText(btnName);
						}
					}
				}
				else if(textField.getText().contains("."))
				{
					if(!btnName.equals("."))
					{
						textField.setText(textField.getText() + btnName);
					}
					else
					{
						/**
						 * TODO 这里是不允许增加小数点操作的，程序应发出警告声
						 */
					}
				}
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String btnName = e.getActionCommand();
			if(btnName.equals("0") || btnName.equals("1") ||
			   btnName.equals("2") || btnName.equals("3") ||
			   btnName.equals("4") || btnName.equals("5") ||
			   btnName.equals("6") || btnName.equals("7") ||
			   btnName.equals("8") || btnName.equals("9") ||
			   btnName.equals("."))
			{
				numInput(btnName);
				/*
				 * 当检测到未输入等号时，将文本框内容赋给上一个运算数变量preOperand，
				 * 否则检测到等号后，文本框输入的数字被作为独立的运算数存储到isoOperand，
				 * 每次输入完一个数之后都将文本框状态设置为非结果的，即等待附加数字的状态
				 */
				if(!eqlSign)
				{
					preOperand = Double.parseDouble(textField.getText());
				}
				else if(eqlSign)
				{
					isoOperand = Double.parseDouble(textField.getText());
				}
				
				state = TfState.NON_RESULT;
			}
			
			/*
			 * 只有四则运算符才被作为运算符，在输入运算符之后，首先检测是否已输入过等号，
			 * 如果是等号：
			 * 然后如果是“非结果”即文本待附加状态，则被认为输入过独立运算数，并将此运算
			 * 数作为上一个运算数，否则如果不存在独立的运算数，则将结果作为上一个运算数，
			 * 接着计算出结果的值；
			 * 否则如果不是等号：
			 * 然后如果是“非结果”即文本待附加状态，则计算出结果的值，其他情况就输出结果
			 * 到文本框。
			 * 等号检测完之后，将当前结果作为上一个操作数，设置等号标记位false，将当前
			 * 输入的符号作为最后的符号标记，最后将文本框状态设置为“结果”即可附加文本
			 */
			if(btnName.equals("+") || btnName.equals("-") || 
					btnName.equals("*") || btnName.equals("/") )
			{
				if(eqlSign)
				{
					if(state == TfState.NON_RESULT)
					{
						preOperand = isoOperand;
					}
					else
					{
						preOperand = result;
					}
					result = operation(sign);
				}
				else
				{
					if(state == TfState.NON_RESULT)
					{
						result = operation(sign);
					}
					outputResult(sign, preOperand);
				}
				
				preOperand = result;
				eqlSign = false;
				sign = btnName;
				state = TfState.RESULT;
			}
			
			//任何时候输入等号后，都会马上计算并输出结果，并设置等号标记和结果状态
			if(btnName.equals("="))
			{
				result = operation(sign);
				outputResult(sign, preOperand);
				eqlSign = true;
				state = TfState.RESULT;
			}
			

			/*
			 * 这里对比Windows计算器，发现Windows计算器有一个bug，当使用运算符输出结果
			 * 后，再输入±，则文本框会输出0，正常情况下应该对显示数进行相反数运算
			 */
			if(btnName.equals("±"))
			{
				textField.setText(formatter.format(0 - Double.parseDouble(textField.getText())));
				preOperand = Double.parseDouble(textField.getText());
			}
			
			/*
			 * 通过这次编程，终于认识到最少使用的百分比其实很有用。
			 * 百分比的主要作用我认为是可以计算一个数的按百分比增长或者减少到多少。
			 * 任何时候百分比操作都会将当前文本框的数更新到preOperand。
			 * 其他倒数和平方根都是同一的道理。
			 */
			if(btnName.equals("%"))
			{
				preOperand *= Double.parseDouble(textField.getText()) / 100;
				textField.setText(formatter.format(preOperand));
				state = TfState.RESULT;
			}
			/**
			 * TODO 需要实现除数不能为0
			 */
			if(btnName.equals("1/x"))
			{
				preOperand = 1 / Double.parseDouble(textField.getText());
				textField.setText(formatter.format(preOperand));
				state = TfState.RESULT;
			}
			if(btnName.equals("sqrt"))
			{
				preOperand = Math.sqrt(Double.parseDouble(textField.getText()));
				textField.setText(formatter.format(preOperand));
				state = TfState.RESULT;
			}
			
			if(btnName.equals("C"))
			{
				textField.setText(INIT_NUM);
				state = TfState.RESULT;
				sign = "";
				eqlSign = false;
				result = 0;
				preOperand = 0;
				isoOperand = 0;
			}
			if(btnName.equals("CE"))
			{
				textField.setText("0");
				state = TfState.RESULT;
			}
			if(btnName.equals("Backspace"))
			{
				if(state == TfState.NON_RESULT)
				{
					backspace();
					if(!eqlSign)
					{
						preOperand = Double.parseDouble(textField.getText());
					}
					else
					{
						isoOperand = Double.parseDouble(textField.getText());
					}
				}
				else
				{
					/**
					 * TODO 这里是不允许退格操作的，GUI应发出警告声
					 */
				}
			}
			
			if(btnName.equals("MS"))
			{
				numRegister = Double.parseDouble(textField.getText());
			}
			if(btnName.equals("M+"))
			{
				numRegister += Double.parseDouble(textField.getText());
			}
			if(btnName.equals("MC"))
			{
				numRegister = 0;
			}
			if(btnName.equals("MR"))
			{
				textField.setText(formatter.format(numRegister));
				if(!eqlSign)
				{
					preOperand = Double.parseDouble(textField.getText());
				}
				else if(eqlSign)
				{
					isoOperand = Double.parseDouble(textField.getText());
				}
				state = TfState.RESULT;
			}
		}
	}
	public static void main(String[] args)
	{
		MyCalculator_V3.visualizer();
	}

}
