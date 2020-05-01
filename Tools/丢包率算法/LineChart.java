package packetLossRate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//�洢����Ҫ���������ͼ����Ϣ��

public class LineChart {
	DateFormat format = new SimpleDateFormat("HH:mm:ss");

	public String date_time = null; // ���ڣ��ꡢ�¡��ա�
	public String big_batch = null; // �����κš�
	public String kind_AIR_GND = null; // ��AIR����GND��
	public int batch = 1; // ͬһ�����£���ʱ��ε����ݴ��䡢�������Ρ�
	public int num_T = -1; // �������롣
	public int num_R = -1; // �������롣

	public int sum_T = -1;
	public int sum_R = -1;
	public Date time_s;

	
	//���SQL��䡣
	public String putSQL(String sql_table_name) {
//		String sql_table_name="packetLossRate";
		//insert into packetLossRate value('2019-10-29','1','AIR_to_GND','1','45','254','09:22:24',4692,53,0.8);

		String sql = "";
		sql="insert into "+sql_table_name;
		
		sql = sql+" value('"+ this.date_time + "','";
		sql = sql + this.big_batch + "','";
		sql = sql + this.kind_AIR_GND + "','";
		sql = sql + this.batch + "','";
		sql = sql + this.num_T + "','";
		sql = sql + this.num_R + "','";

		sql = sql + this.format.format(this.time_s) + "',";
		sql = sql + this.sum_T + ",";
		sql = sql + this.sum_R+ ",";
		double t=this.sum_T;
		double r=this.sum_R;
		r=r/t;
		
		sql = sql +r+ ");";

		return sql;
	}

	

	public LineChart(String date_time, String big_batch, String kind_AIR_GND, int batch, int num_T, int num_R,
			int sum_T, int sum_R, Date time_s) {
		this.date_time = date_time;
		this.big_batch = big_batch;
		this.kind_AIR_GND = kind_AIR_GND;
		this.batch = batch;
		this.num_T = num_T;
		this.num_R = num_R;
		this.sum_T = sum_T;
		this.sum_R = sum_T - sum_R;
		this.time_s = time_s;
	}

}
