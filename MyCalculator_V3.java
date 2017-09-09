/**
 * ������������߼��Ϻ�Windows�������Ѿ�������
 * ��ֿ��ǵ��û����κ������Windows����������ͬ����Ϊ��
 * ���Ǵ���Ƚϳ����������Ż��Ŀ��ܡ�
 * ������ڴ��������Ϊ0���쳣û�кܺõĽ��������
 * ���ı�����ʾ����������Ϊ0��֮��ļ��㶼����һЩ���⣬ԭ��
 * ���ı���ǰ��ʾ�����ַ������ǿɱ�����Ϊ���ֵ��ַ�����
 * ���⣬��û��ʵ��ֻ�������ֺ���������Լ��̵����롣
 * 
 * ��󰡣���������������ϴ�����û�����ˣ�������ۿɲ��ҹ�ά�ˡ�(�s��t,)
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
	
	private static JTextField textField;			//�ı���������λ��BorderLayout.NORTH
	
	private static JPanel panButtonLeftField;		//��ť����λ��BorderLayout.WEST
	
	private static JPanel panButtonRightField;		//��ť����λ��BorderLayout.EAST
	private static JPanel panButtonUpRightField;	//��ť����λ��BorderLayout.EAST��UP����
	private static JPanel panButtonDownRightField;	//��ť����λ��BorderLayout.EAST��Down����
	
	/*
	 * ����ÿ����ť
	 */
	private static JButton btn_void, btn_MC, btn_MR, btn_MS, btn_Mplus;
	private static JButton btn_Backspace, btn_CE, btn_C;
	private static JButton btn_7, btn_8, btn_9, btn_4, btn_5, btn_6, 
							btn_1, btn_2, btn_3, btn_0, btn_plusOrminus, btn_dot;
	private static JButton btn_div, btn_mul, btn_sub, btn_plus, btn_sqrt, btn_per, btn_reci, btn_eql;
	
	/*
	 * ��̬��ʼ��ÿ����ť
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
		btn_0 = new JButton("0"); btn_plusOrminus = new JButton("��"); btn_dot = new JButton(".");
		
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
	 * �ı����״̬�����֣�����ͷǽ��
	 * ������ж�Ϊ���״̬����ͨ�����룬�ı��������ֱ�Ӹ���				
	 * ��������������ݸ��ӵ��ı���ԭ���ı��ĺ��棬������numInput()����ʵ��
	 */
	private static enum TfState
	{
		RESULT, NON_RESULT
	}
	static TfState state = TfState.RESULT;
	
	/*
	 * ��ʼ���������ܾ��ø���ô�ྲ̬������̫�׵���...
	 * result�����������������ı���preOperand����Ϊ��һ������������Ҫ����
	 * ��������iosOperand��һ������������Ⱥ�֮��洢�Ķ�������������eqlSign
	 * ���ڴ洢�Ƿ���������Ⱥŵı�ǣ�numRegister�����ּĴ������洢M��������
	 */
	private static final String INIT_NUM = "0";
	static double result = 0;
	static double preOperand = 0;
	static double isoOperand = 0;
	static boolean eqlSign = false;
	static String sign = "";
	
	static double numRegister = 0;
	
	//��ʽ��������ʽ��Ϊֻ����3λС������ȥ�����õ�0
	static DecimalFormat formatter = new DecimalFormat("#.###");
	
	public static void visualizer()
	{
		frame = new JFrame("MyCalculator");
		
		textField = new JTextField();
		
		//M����������λ5��1��
		panButtonLeftField = new JPanel();
		panButtonLeftField.setLayout(new GridLayout(5, 1));
		
		//��Ӱ�����M������
		btn_void.setEnabled(false);
		panButtonLeftField.add(btn_void);
		panButtonLeftField.add(btn_MC);
		panButtonLeftField.add(btn_MR);
		panButtonLeftField.add(btn_MS);
		panButtonLeftField.add(btn_Mplus);
		
		//M�����ұ�����
		panButtonRightField = new JPanel();
		panButtonRightField.setLayout(new BorderLayout());
		
		//M���ұ�������ϲ��ֵ�������˸����Ϊ1��3��
		panButtonUpRightField = new JPanel();
		panButtonUpRightField.setLayout(new GridLayout(1, 3));
		
		panButtonUpRightField.add(btn_Backspace);
		panButtonUpRightField.add(btn_CE);
		panButtonUpRightField.add(btn_C);
		
		//���ֺ����������
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
		
		//����������Ҫ�Ƿ���ʹ��ѭ������Ӽ��̼�����
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
		
		//��M���Ҳ����������Ϊ�����˸���������������������
		panButtonRightField.add(panButtonUpRightField, BorderLayout.NORTH);
		panButtonRightField.add(panButtonDownRightField, BorderLayout.SOUTH);
		
		//�ı�����������frame�ı���M��������frame������ʣ�µķ��õ�������
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
	 * ���еİ�����������ͬһ�������������У��������˼�������
	 * ����������Ŀ�Ķ���Ϊ�˾����ܼ����ظ��Ĵ��룬��߿ɶ���
	 */
	public static class ButtonListener implements ActionListener
	{
		public void outputResult(String sign, double preOperand)
		{
			if(sign.equals("/") && preOperand == 0)
			{
				textField.setText("��������Ϊ0");
				/**
				 * TODO �ڴ�֮��Ӧ�ò��ܽ����κ����룬��C����CE��֮��
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
		 * �������ֺ�С����������Ӱ�����أ�1.�ı����Ƿ�����С���㣻
		 * 2.��ǰ������Ƿ���С���㣻3.�ı��������Ƿ����0��
		 * ���⣬��������ǰ���ı���״̬������ͷǽ����Ҳ��������ͬ��
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
						 * TODO �����ǲ���������С��������ģ�����Ӧ����������
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
				 * ����⵽δ����Ⱥ�ʱ�����ı������ݸ�����һ������������preOperand��
				 * �����⵽�Ⱥź��ı�����������ֱ���Ϊ�������������洢��isoOperand��
				 * ÿ��������һ����֮�󶼽��ı���״̬����Ϊ�ǽ���ģ����ȴ��������ֵ�״̬
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
			 * ֻ������������ű���Ϊ������������������֮�����ȼ���Ƿ���������Ⱥţ�
			 * ����ǵȺţ�
			 * Ȼ������ǡ��ǽ�������ı�������״̬������Ϊ���������������������������
			 * ����Ϊ��һ����������������������ڶ��������������򽫽����Ϊ��һ����������
			 * ���ż���������ֵ��
			 * ����������ǵȺţ�
			 * Ȼ������ǡ��ǽ�������ı�������״̬�������������ֵ�����������������
			 * ���ı���
			 * �Ⱥż����֮�󣬽���ǰ�����Ϊ��һ�������������õȺű��λfalse������ǰ
			 * ����ķ�����Ϊ���ķ��ű�ǣ�����ı���״̬����Ϊ����������ɸ����ı�
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
			
			//�κ�ʱ������Ⱥź󣬶������ϼ��㲢�������������õȺű�Ǻͽ��״̬
			if(btnName.equals("="))
			{
				result = operation(sign);
				outputResult(sign, preOperand);
				eqlSign = true;
				state = TfState.RESULT;
			}
			

			/*
			 * ����Ա�Windows������������Windows��������һ��bug����ʹ�������������
			 * ��������������ı�������0�����������Ӧ�ö���ʾ�������෴������
			 */
			if(btnName.equals("��"))
			{
				textField.setText(formatter.format(0 - Double.parseDouble(textField.getText())));
				preOperand = Double.parseDouble(textField.getText());
			}
			
			/*
			 * ͨ����α�̣�������ʶ������ʹ�õİٷֱ���ʵ�����á�
			 * �ٷֱȵ���Ҫ��������Ϊ�ǿ��Լ���һ�����İ��ٷֱ��������߼��ٵ����١�
			 * �κ�ʱ��ٷֱȲ������Ὣ��ǰ�ı���������µ�preOperand��
			 * ����������ƽ��������ͬһ�ĵ���
			 */
			if(btnName.equals("%"))
			{
				preOperand *= Double.parseDouble(textField.getText()) / 100;
				textField.setText(formatter.format(preOperand));
				state = TfState.RESULT;
			}
			/**
			 * TODO ��Ҫʵ�ֳ�������Ϊ0
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
					 * TODO �����ǲ������˸�����ģ�GUIӦ����������
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
