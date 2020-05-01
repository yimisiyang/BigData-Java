package packetLossRate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class packetLossRate {

	// ��¼ÿ��ʱ���Ķ�����Ϣ��
	public List<everyPacketLossRate> listEveryPacketLossRate = new ArrayList<>();

	// ��¼������ ������ ���� С���� ����Դ ����Դ ��Ϣ��
	public String sql01 = "";

	// .rate�ļ� �ָ�����
	public String str_split = "\t";
	public String str_split2 = "_";
	// ����ͼ�ֵĵ�����
	public long lineChart = 20;
	public String AIR_toLowerCase = "air";
	public String GND_toLowerCase = "gnd";
	public int MAX_int_PDU = 65535;
	public int t_timeOver = 60 * 1000;

	// ʱ���ʽ StringתDate
	DateFormat format = new SimpleDateFormat("HH:mm:ss");

	// ��¼�����㡣
	public HashMap<String, String> hashMap = new HashMap<>();
	// ��ArrayList���洢���������ݡ�
	List<inputHashMap> list = new ArrayList<>();
	// �洢���յ�����ͼ����Ϣ��
	public List<LineChart> list_LineChart = new ArrayList<>();

	public List<T_and_R> list_big_batch = new ArrayList<>();
	public List<List<T_and_R>> list_date_time = new ArrayList<>();

	// �жϱ�
	public int[] judge;
	public int[] small = {
			/* 0 1 2 3 4 5 6 7 8 9 */
			// 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6
			/* 0 */ 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0,
			1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, /* 8 */ 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public int[] mid = {
			/* 0 1 2 3 4 5 6 7 8 9 */
			// 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6
			/* 0 */ 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
			1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 8 */ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public int[] big = {
			/* 0 1 2 3 4 5 6 7 8 9 */
			// 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6
			/* 0 */ 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
			1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1,
			0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, /* 8 */ 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 };

	class everyPacketLossRate {
		// ��¼ÿ��ʱ���Ķ����ʡ�
		public String time_T = null;
		public String time_R = null;
		public int packetLossRateState = -1;

		public everyPacketLossRate(String time_T, String time_R, int packetLossRateState) {
			super();
			this.time_T = time_T;
			this.time_R = time_R;
			this.packetLossRateState = packetLossRateState;
		}

	}

	// �洢T��R.rate�ļ���Ϣ��
	class T_and_R {
		public String big_batch = null; // �����κš�
		public String date_time = null; // ���ڣ��ꡢ�¡��ա�
		public int num = -1; // �������롣
		public String kind_AIR_GND = null; // ��AIR����GND��
		public int batch = 1; // ͬһ�����£���ʱ��ε����ݴ��䡢�������Ρ�
		public String T_path = null;
		public String R_path = null;

		public T_and_R(int num, String kind_AIR_GND, String T_path, String R_path, int batch, String big_batch,
				String date_time) {
			this.big_batch = big_batch;
			this.date_time = date_time;
			this.num = num;
			this.kind_AIR_GND = kind_AIR_GND;
			this.batch = batch;
			this.T_path = T_path;
			this.R_path = R_path;
		}

	}

	// �洢��������Ϣ��
	class inputHashMap {
		public String key = null;
		public String value = null;

		public inputHashMap(String key, String value) {
			this.key = key;
			this.value = value;
		}

	}

	public void init_hashMap() {
		this.hashMap.clear();
		for (int i = 0; i < this.list.size(); i++) {
			this.hashMap.put(this.list.get(i).key, this.list.get(i).value);
		}
	}

	public void findReboot(File readPath_AIR_T, int MAX_int_PDU) throws IOException {

		this.list.clear();

		BufferedReader br_AIR_T = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_AIR_T), "gbk"));
		// �������õı�ͷ��
		String lineTxt = br_AIR_T.readLine();
		String str_t;
		int T_PDU_2_old = -1;
		if ((str_t = lineTxt = br_AIR_T.readLine()) != null) {
			String[] Str_AIR_T = lineTxt.split(this.str_split);
			T_PDU_2_old = Integer.parseInt(Str_AIR_T[2]);
		}
		while ((lineTxt = br_AIR_T.readLine()) != null) {
			String[] Str_AIR_T = lineTxt.split(this.str_split);
			if (1 != (Integer.parseInt(Str_AIR_T[2]) - T_PDU_2_old)) {
				if ((T_PDU_2_old != MAX_int_PDU) && (Integer.parseInt(Str_AIR_T[2]) == 1)) {
					this.list.add(new inputHashMap(str_t, Str_AIR_T[3]));
					// System.out.print(readPath_AIR_T.getName() + "\t");
					// System.out.println(str_t);
				}
			}
			T_PDU_2_old = Integer.parseInt(Str_AIR_T[2]);
			str_t = lineTxt;
		}
		br_AIR_T.close();
	}

	public void t_select(String t) {
		if ("small".equals(t.toLowerCase())) {
			this.judge = this.small;
			return;
		}
		if ("mid".equals(t.toLowerCase())) {
			this.judge = this.mid;
			return;
		}
		if ("big".equals(t.toLowerCase())) {
			this.judge = this.big;
			return;
		}

	}

	public boolean t_TimeGap(int t) {
		// ʱ϶��ɸѡ��
		if (0 == this.judge[t]) {
			return false;
		} else {
			return true;
		}

	}

	public void Calculation_AIR_to_AIR(File readPath_AIR_T, File readPath_GND_R, String AIR_T_Դ��ַ_1, int t_��ʱֵ)
			throws IOException, ParseException {

		boolean reboot = false;
		String reboot_time = "";
		String str_CRC_5 = "��ȷ";
		String AIR_T_PDU���к�_2;
		Date AIR_T_ʱ���_3;
		this.listEveryPacketLossRate.clear();

		BufferedReader br_AIR_T = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_AIR_T), "gbk"));
		BufferedReader br_GND_R = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_GND_R), "gbk"));

		// �������õı�ͷ��
		String lineTxt_br_AIR_T = br_AIR_T.readLine();

		while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
			String[] Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

			// �ж��Ƿ�������
			if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

				// System.out.println("�����ˣ�����");
				// �����С�
				// System.out.println();
				// System.out.println();
				if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
					String[] t = lineTxt_br_AIR_T.split(this.str_split);
					reboot_time = t[3];
				} else {
					// �������˳���
					break;
				}
				reboot = true;
				continue;
			}

			// �жϷ��ͷ���CRC��ʶ��
			if (!str_CRC_5.equals(Str_AIR_T[5])) {
				continue;
			}
			if (!this.t_TimeGap(Integer.parseInt(Str_AIR_T[4]))) {
				// �����������Ϳ���������ݡ�
				continue;
			}

			AIR_T_PDU���к�_2 = Str_AIR_T[2];
			AIR_T_ʱ���_3 = this.format.parse(Str_AIR_T[3]);
			String lineTxt_br_GND_R = null;
			while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
				String[] Str_GND_R = lineTxt_br_GND_R.split(this.str_split);
				// ���жϡ�Դ��ַ�������жϡ�PDU���кš�������жϡ�ʱ������롰CRC��ʶ��

				// ����ʱ���жϡ�
				if (reboot) {
					if ((this.format.parse(Str_GND_R[3]).getTime() - this.format.parse(reboot_time).getTime()) <= 0) {
						while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
							String[] t = lineTxt_br_GND_R.split(this.str_split);
							// ��ȡ����֮������ݡ�
							if ((this.format.parse(t[3]).getTime() - this.format.parse(reboot_time).getTime()) > 0) {
								reboot = false;
								break;
							}
						}
						// R�������˳���
						if (lineTxt_br_GND_R == null) {
							break;
						}
					}
				}

				if (AIR_T_Դ��ַ_1.equals(Str_GND_R[1])) {

					// T��������ҲҪУ�顰ʱ϶�š�������
					if (!this.t_TimeGap(Integer.parseInt(Str_GND_R[4]))) {
						// �����������Ϳ���������ݡ�
						continue;
					}

					if (AIR_T_PDU���к�_2.equals(Str_GND_R[2])) {
						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_ʱ���_3.getTime()) <= t_��ʱֵ)) {
							// �ɹ����յ����ݡ�

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 1));
							// this.writerδ����(Str_GND_R);
							break;
						} else {
							// ��ʱ�����ݰ�����
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 0));

							break;
						}

					} else {
						// δ���յ��������ݡ�

						if ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_ʱ���_3.getTime()) < 0) {

							// ��ʷ���ݡ���T��δ���ͣ�R�ͽ��յ������ˣ�
							continue;
						}

						this.listEveryPacketLossRate.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), "NA", 0));

						while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
							Str_AIR_T = null;
							Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

							// �ж��Ƿ�������
							if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

								// �����С�
								// System.out.println();
								// System.out.println();
								if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
									String[] t = lineTxt_br_AIR_T.split(this.str_split);
									reboot_time = t[3];
									reboot = true;
									break;
								} else {
									// �������˳���
									break;
								}

							}

							// �жϷ��ͷ���CRC��ʶ��
							if (!str_CRC_5.equals(Str_AIR_T[5])) {
								continue;
							}
							if (!this.t_TimeGap(Integer.parseInt(Str_AIR_T[4]))) {
								// �����������Ϳ���������ݡ�
								continue;
							}

							AIR_T_PDU���к�_2 = Str_AIR_T[2];
							AIR_T_ʱ���_3 = this.format.parse(Str_AIR_T[3]);

							if (Str_GND_R[2].equals(AIR_T_PDU���к�_2)) {
								// ���ܻ��ٴγ���T�󷢣�R���յ������쳣�����
								break;
							} else {
								// ����������

								this.listEveryPacketLossRate
										.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), "NA", 0));
							}
						}

						if (lineTxt_br_AIR_T == null) {
							// T�ļ��������ˣ�Ҫ�˳���
							break;
						}
						if (reboot) {
							break;
						}

						// �ٴ��ж��Ƿ�ʱ�����ݰ�����
						// �ٴ��ж��Ƿ�ʱ�����ݰ�����

						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_ʱ���_3.getTime()) <= t_��ʱֵ)) {
							// �ɹ����յ����ݡ�
							// this.AIR_T_count++;
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 1));

							break;
						} else {
							// ��ʱ�����ݰ�����

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 0));

							break;
						}

					}
				} else {
					// ��������ض����ͷ����������ݣ�ֱ��������һ�����ݡ�
					continue;
				}

			}
		}
		br_AIR_T.close();
		br_GND_R.close();

	}

	public void Calculation_AIR_to_GND(File readPath_AIR_T, File readPath_GND_R, String AIR_T_Դ��ַ_1, int t_��ʱֵ)
			throws IOException, ParseException {

		boolean reboot = false;
		String reboot_time = "";
		String str_CRC_5 = "��ȷ";
		String AIR_T_PDU���к�_2;
		Date AIR_T_ʱ���_3;
		this.listEveryPacketLossRate.clear();

		BufferedReader br_AIR_T = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_AIR_T), "gbk"));
		BufferedReader br_GND_R = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_GND_R), "gbk"));

		// �������õı�ͷ��
		String lineTxt_br_AIR_T = br_AIR_T.readLine();

		while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
			String[] Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

			// �ж��Ƿ�������
			if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

				// System.out.println("�����ˣ�����");
				// �����С�
				// System.out.println();
				// System.out.println();
				if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
					String[] t = lineTxt_br_AIR_T.split(this.str_split);
					reboot_time = t[3];
				} else {
					// �������˳���
					break;
				}
				reboot = true;
				continue;
			}

			// �жϷ��ͷ���CRC��ʶ��
			if (!str_CRC_5.equals(Str_AIR_T[5])) {
				continue;
			}
			// if (!this.t_ʱ϶��(Integer.parseInt(Str_AIR_T[4]))) {
			// // �����������Ϳ���������ݡ�
			// continue;
			// }

			AIR_T_PDU���к�_2 = Str_AIR_T[2];
			AIR_T_ʱ���_3 = this.format.parse(Str_AIR_T[3]);
			String lineTxt_br_GND_R = null;
			while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
				String[] Str_GND_R = lineTxt_br_GND_R.split(this.str_split);
				// ���жϡ�Դ��ַ�������жϡ�PDU���кš�������жϡ�ʱ������롰CRC��ʶ��

				// ����ʱ���жϡ�
				if (reboot) {
					if ((this.format.parse(Str_GND_R[3]).getTime() - this.format.parse(reboot_time).getTime()) <= 0) {
						while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
							String[] t = lineTxt_br_GND_R.split(this.str_split);
							// ��ȡ����֮������ݡ�
							if ((this.format.parse(t[3]).getTime() - this.format.parse(reboot_time).getTime()) > 0) {
								reboot = false;
								break;
							}
						}
						// R�������˳���
						if (lineTxt_br_GND_R == null) {
							break;
						}
					}
				}

				if (AIR_T_Դ��ַ_1.equals(Str_GND_R[1])) {

					// T��������ҲҪУ�顰ʱ϶�š�������
					// if (!this.t_ʱ϶��(Integer.parseInt(Str_GND_R[4]))) {
					// // �����������Ϳ���������ݡ�
					// continue;
					// }

					if (AIR_T_PDU���к�_2.equals(Str_GND_R[2])) {
						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_ʱ���_3.getTime()) <= t_��ʱֵ)) {
							// �ɹ����յ����ݡ�

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 1));
							// this.writerδ����(Str_GND_R);
							break;
						} else {
							// ��ʱ�����ݰ�����

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 0));

							break;
						}

					} else {
						// δ���յ��������ݡ�

						if ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_ʱ���_3.getTime()) < 0) {

							// ��ʷ���ݡ���T��δ���ͣ�R�ͽ��յ������ˣ�
							continue;
						}

						this.listEveryPacketLossRate.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), "NA", 0));

						while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
							Str_AIR_T = null;
							Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

							// �ж��Ƿ�������
							if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

								// �����С�
								// System.out.println();
								// System.out.println();
								if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
									String[] t = lineTxt_br_AIR_T.split(this.str_split);
									reboot_time = t[3];
									reboot = true;
									break;
								} else {
									// �������˳���
									break;
								}

							}

							// �жϷ��ͷ���CRC��ʶ��
							if (!str_CRC_5.equals(Str_AIR_T[5])) {
								continue;
							}
							// if (!this.t_ʱ϶��(Integer.parseInt(Str_AIR_T[4])))
							// {
							// // �����������Ϳ���������ݡ�
							// continue;
							// }

							AIR_T_PDU���к�_2 = Str_AIR_T[2];
							AIR_T_ʱ���_3 = this.format.parse(Str_AIR_T[3]);

							if (Str_GND_R[2].equals(AIR_T_PDU���к�_2)) {
								// ���ܻ��ٴγ���T�󷢣�R���յ������쳣�����
								break;
							} else {
								// ����������

								this.listEveryPacketLossRate
										.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), "NA", 0));

							}
						}

						if (lineTxt_br_AIR_T == null) {
							// T�ļ��������ˣ�Ҫ�˳���

							break;
						}
						if (reboot) {
							break;
						}

						// �ٴ��ж��Ƿ�ʱ�����ݰ�����
						// �ٴ��ж��Ƿ�ʱ�����ݰ�����

						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_ʱ���_3.getTime()) <= t_��ʱֵ)) {
							// �ɹ����յ����ݡ�
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 1));
							//
							// this.writerδ����(Str_GND_R);
							break;
						} else {
							// ��ʱ�����ݰ�����
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_ʱ���_3), Str_GND_R[3], 0));

							break;
						}

					}
				} else {
					// ��������ض����ͷ����������ݣ�ֱ��������һ�����ݡ�
					continue;
				}

			}
		}
		br_AIR_T.close();
		br_GND_R.close();

	}

	public void calculateLineChart(String date_time, String big_batch, String kind_AIR_GND, int batch, int num_T,
			int num_R) throws ParseException {

		// �����ݣ�ֱ���˳� ��ĳ����û�н��ܵ���һ���͵����ݣ����Ͳ������ˡ�
		if (this.listEveryPacketLossRate.size() == 0) {
			return;
		}

		Date time_S;
		Date time_E;
		// �洢�ֶε�ʱ��㡣
		List<Date> list_Date = new ArrayList<>();

		time_S = this.format.parse(this.listEveryPacketLossRate.get(0).time_T);
		time_E = this.format.parse(this.listEveryPacketLossRate.get(this.listEveryPacketLossRate.size() - 1).time_T);

		long duan = time_E.getTime() - time_S.getTime();
		long duan_step = duan / (this.lineChart);

		// ����ʱ�䣬Ӧ�ü�1�롣
		for (long i = 0; i < this.lineChart; i++) {
			Date time_ = new Date(time_S.getTime() + i * duan_step + 1000);
			list_Date.add(time_);
		}
		list_Date.add(new Date(time_E.getTime() + 1000));

		Date time_temp;
		int state = 0;
		int sum_T = 0;
		int sum_R = 0;

		int j = 0;

		for (int i = 1; i < list_Date.size(); i++) {
			for (; j < this.listEveryPacketLossRate.size(); j++) {
				time_temp = this.format.parse(this.listEveryPacketLossRate.get(j).time_T);
				if ((list_Date.get(i).getTime() - time_temp.getTime()) > 0) {
					// �Ǳ�ʱ��εġ�
					sum_R = sum_R + this.listEveryPacketLossRate.get(j).packetLossRateState;
					sum_T++;
				} else {
					// ����һ��ʱ��Ρ�
					this.list_LineChart.add(new LineChart(date_time, big_batch, kind_AIR_GND, batch, num_T, num_R,
							sum_T, sum_R, new Date(list_Date.get(i).getTime() - 1000)));
					// ��ȡ���еĶ�������Ϣ��
					sum_R = 0;
					sum_T = 0;
					break;
				}

			}
		}
		this.list_LineChart
				.add(new LineChart(date_time, big_batch, kind_AIR_GND, batch, num_T, num_R, sum_T, sum_R, time_E));

	}

	public void readPathFile(File readPath) {

		String dataPDU = "";
		String stitching = "-";

		File[] DateName = readPath.listFiles();
		// �ж϶���Ŀ¼�Ƿ�Ϸ���
		if (DateName.length != 1) {
			System.out.println("��Ŀ¼Ϊ�ջ��ж�������ļ���");
			return;
		} else {
			String time = DateName[0].getName();
			if (time.length() != 8) {
				System.out.println("�޷��ҵ��Ϸ��������ļ���");
				return;
			} else {
				// ��ȡ�ꡢ�¡���ʱ�����
				dataPDU = time.substring(0, 4) + stitching + time.substring(4, 6) + stitching + time.substring(6, 8);
			}
		}

		File[] files_big_batch = DateName[0].listFiles();
		// files_big_batch�Ǹ���������Ŀ¼��
		for (int i = 0; i < files_big_batch.length; i++) {

			// kind_AIR_GND��AIR��GND�ļ��С�
			File[] kind_AIR_GND = files_big_batch[i].listFiles();

			for (int j = 0; j < kind_AIR_GND.length; j++) {
				// ת��ΪСд���ļ�����
				String str_kind = kind_AIR_GND[j].getName().toLowerCase();
				// ���ա�_"�ָ��ļ�����
				String[] str_kind_s = str_kind.split(this.str_split2);
				if (str_kind_s.length > 1) {
					// һ����GND_*
					// ��ȡGND��T��R�ļ���
					File[] T_R_rate_s = kind_AIR_GND[j].listFiles();

					String[] num_GND = T_R_rate_s[0].getName().split(this.str_split2);
					String str_T = null;
					String str_R = T_R_rate_s[0].getPath();

					this.list_big_batch.add(new T_and_R(Integer.parseInt(num_GND[0]), "GND", str_T, str_R,
							Integer.parseInt(str_kind_s[1]), files_big_batch[i].getName(), dataPDU));

				} else {
					// ��AIR��GND�ļ��С�
					if (this.AIR_toLowerCase.equals(kind_AIR_GND[j].getName().toLowerCase())) {
						// ��AIR�ļ���
						// AIR_file_s��AIR�ļ����µķɻ�����ļ��С�
						File[] AIR_file_s = kind_AIR_GND[j].listFiles();

						// �������е�AIR�ļ���
						for (int k = 0; k < AIR_file_s.length; k++) {
							// �����ж�AIR��С���Ρ�
							// ���κ����ļ�����
							String[] str_name = AIR_file_s[k].getName().split(this.str_split2);
							// T_R_rate_s��T��R�ļ���
							File[] T_R_rate_s = AIR_file_s[k].listFiles();
							String str_T = null;
							String str_R = null;
							if (str_name.length > 1) {
								// ��С���Ρ�
								for (int ii = 0; ii < T_R_rate_s.length; ii++) {
									// ת��Ϊ��д��ĸ������"_"�ִ� ����101_R.rate
									String[] str__ = T_R_rate_s[ii].getName().toUpperCase().split(this.str_split2);
									String T_or_R = str__[1].substring(0, 1);

									if ("T".equals(T_or_R)) {
										str_T = T_R_rate_s[ii].getPath();
									} else {
										str_R = T_R_rate_s[ii].getPath();
									}
								}
								this.list_big_batch.add(new T_and_R(Integer.parseInt(str_name[0]), "AIR", str_T, str_R,
										Integer.parseInt(str_name[1]), files_big_batch[i].getName(), dataPDU));

							} else {
								for (int ii = 0; ii < T_R_rate_s.length; ii++) {
									// ת��Ϊ��д��ĸ������"_"�ִ� ����101_R.rate
									String[] str__ = T_R_rate_s[ii].getName().toUpperCase().split(this.str_split2);
									String T_or_R = str__[1].substring(0, 1);
									if ("T".equals(T_or_R)) {
										str_T = T_R_rate_s[ii].getPath();
									} else {
										str_R = T_R_rate_s[ii].getPath();
									}
								}
								this.list_big_batch.add(new T_and_R(Integer.parseInt(str_name[0]), "AIR", str_T, str_R,
										1, files_big_batch[i].getName(), dataPDU));

							}
						}
					} else {
						// ��GND�ļ��С�
						File[] T_R_rate_s = kind_AIR_GND[j].listFiles();

						String[] name_GND = T_R_rate_s[0].getName().split(this.str_split2);

						int num_GND = Integer.parseInt(name_GND[0]);
						String str_T = null;
						String str_R = T_R_rate_s[0].getPath();

						this.list_big_batch.add(
								new T_and_R(num_GND, "GND", str_T, str_R, 1, files_big_batch[i].getName(), dataPDU));

					}
				}

			}

			List<T_and_R> list_t = new ArrayList<>();
			list_t.addAll(this.list_big_batch);
			this.list_date_time.add(list_t);
			this.list_big_batch.clear();

		}

	}

	public void CartesianProduct() throws IOException, ParseException {

		for (int i = 0; i < this.list_date_time.size(); i++) {
			for (int j = 0; j < this.list_date_time.get(i).size(); j++) {
				for (int k = 0; k < this.list_date_time.get(i).size(); k++) {
					int num01 = this.list_date_time.get(i).get(j).num;
					String kind_AIR_GND01 = this.list_date_time.get(i).get(j).kind_AIR_GND;
					int batch01 = this.list_date_time.get(i).get(j).batch;
					String T_path01 = this.list_date_time.get(i).get(j).T_path;

					int num02 = this.list_date_time.get(i).get(k).num;
					String kind_AIR_GND02 = this.list_date_time.get(i).get(k).kind_AIR_GND;
					int batch02 = this.list_date_time.get(i).get(k).batch;
					String R_path02 = this.list_date_time.get(i).get(k).R_path;

					String big_batch01 = this.list_date_time.get(i).get(j).big_batch;
					String date_time01 = this.list_date_time.get(i).get(j).date_time;

					if (this.AIR_toLowerCase.equals(kind_AIR_GND01.toLowerCase()) && (batch01 == batch02)
							&& (num01 != num02)) {
						// �ǿջ����͡�С�����ͬ��������Ų�һ����
						if (this.AIR_toLowerCase.equals(kind_AIR_GND02.toLowerCase())) {
							// �նԿա�

							// 1����T�������ļ����������㣨������쳣��������PDU�ţ���
							this.findReboot(new File(T_path01), this.MAX_int_PDU);

							// 2����ʼ��init_hashMap��
							this.init_hashMap();

							// 3������ʱ϶�����
							this.t_select("big");

							//
							// 4�����㶪���ʲ�����csv�ļ���

							this.Calculation_AIR_to_AIR(new File(T_path01), new File(R_path02), ("" + num01),
									this.t_timeOver);

							// 5�������ʱ��εĶ����ʡ�

							this.calculateLineChart(date_time01, big_batch01, "AIR_to_AIR", batch01, num01, num02);

						} else {
							// �նԵء�
							// 1

							this.findReboot(new File(T_path01), this.MAX_int_PDU);

							// 2
							this.init_hashMap();

							// 3

							// 4

							this.Calculation_AIR_to_GND(new File(T_path01), new File(R_path02), ("" + num01),
									this.t_timeOver);

							// 5

							this.calculateLineChart(date_time01, big_batch01, "AIR_to_GND", batch01, num01, num02);

						}
					}

				}
			}
		}
	}

	public void putLineChartInformation(String sql_table_name) {
		for (int i = 0; i < this.list_LineChart.size(); i++) {
			System.out.println(this.list_LineChart.get(i).putSQL(sql_table_name));
		}
	}

	public void putIntoMysql(String sql_table_name, String DB_URL, String USER, String PASS)
			throws ClassNotFoundException, SQLException {

		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		

		Connection conn = null; 
		Statement stmt = null; 
		Class.forName(JDBC_DRIVER);
		conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = (Statement) conn.createStatement();

		for (int i = 0; i < this.list_LineChart.size(); i++) {
			stmt.executeUpdate(this.list_LineChart.get(i).putSQL(sql_table_name));
		}

		stmt.close();
		conn.close();

	}

	//ֻ���SQL��䡣
	public void mainFunction(File readPath, String sql_table_name) throws IOException, ParseException {
		// �ļ���·����mysql������
		this.readPathFile(readPath);
		this.CartesianProduct();
		this.putLineChartInformation(sql_table_name);
	}

//	��mysql�в������ݡ�
	public void mainFunction(File readPath, String sql_table_name, String DB_URL, String USER, String PASS)
			throws IOException, ParseException, ClassNotFoundException, SQLException {
		// �ļ���·����mysql������mysql��ַ��mysql�û�����mysql���롣
		this.readPathFile(readPath);
		this.CartesianProduct();
		this.putIntoMysql(sql_table_name, DB_URL, USER, PASS);
	}

	public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException, SQLException {
		packetLossRate a = new packetLossRate();
		// a.mainFunction(new// File("D:/PDU"),"packetLossRate");
		a.mainFunction(new File("D:/PDU"), "packetLossRate","jdbc:mysql://192.168.1.120:3306/test", "root", "");

		System.out.println("������ɣ�");

	}

}
