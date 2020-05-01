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

	// 记录每个时间点的丢包信息。
	public List<everyPacketLossRate> listEveryPacketLossRate = new ArrayList<>();

	// 记录：日期 大批次 类型 小批次 发送源 接收源 信息。
	public String sql01 = "";

	// .rate文件 分隔符。
	public String str_split = "\t";
	public String str_split2 = "_";
	// 折线图分的点数。
	public long lineChart = 20;
	public String AIR_toLowerCase = "air";
	public String GND_toLowerCase = "gnd";
	public int MAX_int_PDU = 65535;
	public int t_timeOver = 60 * 1000;

	// 时间格式 String转Date
	DateFormat format = new SimpleDateFormat("HH:mm:ss");

	// 记录重启点。
	public HashMap<String, String> hashMap = new HashMap<>();
	// 用ArrayList来存储启动点数据。
	List<inputHashMap> list = new ArrayList<>();
	// 存储最终的折线图点信息。
	public List<LineChart> list_LineChart = new ArrayList<>();

	public List<T_and_R> list_big_batch = new ArrayList<>();
	public List<List<T_and_R>> list_date_time = new ArrayList<>();

	// 判断表。
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
		// 记录每个时间点的丢包率。
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

	// 存储T与R.rate文件信息。
	class T_and_R {
		public String big_batch = null; // 大批次号。
		public String date_time = null; // 日期：年、月、日。
		public int num = -1; // 机器编码。
		public String kind_AIR_GND = null; // 是AIR还是GND。
		public int batch = 1; // 同一批次下，分时间段的数据传输、接收批次。
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

	// 存储重启点信息。
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
		// 处理无用的表头。
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
		// 时隙号筛选。
		if (0 == this.judge[t]) {
			return false;
		} else {
			return true;
		}

	}

	public void Calculation_AIR_to_AIR(File readPath_AIR_T, File readPath_GND_R, String AIR_T_源地址_1, int t_超时值)
			throws IOException, ParseException {

		boolean reboot = false;
		String reboot_time = "";
		String str_CRC_5 = "正确";
		String AIR_T_PDU序列号_2;
		Date AIR_T_时间戳_3;
		this.listEveryPacketLossRate.clear();

		BufferedReader br_AIR_T = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_AIR_T), "gbk"));
		BufferedReader br_GND_R = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_GND_R), "gbk"));

		// 处理无用的表头。
		String lineTxt_br_AIR_T = br_AIR_T.readLine();

		while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
			String[] Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

			// 判断是否重启。
			if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

				// System.out.println("重启了！！！");
				// 空两行。
				// System.out.println();
				// System.out.println();
				if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
					String[] t = lineTxt_br_AIR_T.split(this.str_split);
					reboot_time = t[3];
				} else {
					// 无数据退出。
					break;
				}
				reboot = true;
				continue;
			}

			// 判断发送方的CRC标识。
			if (!str_CRC_5.equals(Str_AIR_T[5])) {
				continue;
			}
			if (!this.t_TimeGap(Integer.parseInt(Str_AIR_T[4]))) {
				// 不符合条件就跨过这条数据。
				continue;
			}

			AIR_T_PDU序列号_2 = Str_AIR_T[2];
			AIR_T_时间戳_3 = this.format.parse(Str_AIR_T[3]);
			String lineTxt_br_GND_R = null;
			while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
				String[] Str_GND_R = lineTxt_br_GND_R.split(this.str_split);
				// 先判断“源地址”，再判断“PDU序列号”，最后判断“时间戳”与“CRC标识”

				// 重启时间判断。
				if (reboot) {
					if ((this.format.parse(Str_GND_R[3]).getTime() - this.format.parse(reboot_time).getTime()) <= 0) {
						while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
							String[] t = lineTxt_br_GND_R.split(this.str_split);
							// 获取重启之后的数据。
							if ((this.format.parse(t[3]).getTime() - this.format.parse(reboot_time).getTime()) > 0) {
								reboot = false;
								break;
							}
						}
						// R无数据退出。
						if (lineTxt_br_GND_R == null) {
							break;
						}
					}
				}

				if (AIR_T_源地址_1.equals(Str_GND_R[1])) {

					// T接收数据也要校验“时隙号”！！！
					if (!this.t_TimeGap(Integer.parseInt(Str_GND_R[4]))) {
						// 不符合条件就跨过这条数据。
						continue;
					}

					if (AIR_T_PDU序列号_2.equals(Str_GND_R[2])) {
						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_时间戳_3.getTime()) <= t_超时值)) {
							// 成功接收到数据。

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 1));
							// this.writer未丢包(Str_GND_R);
							break;
						} else {
							// 超时或数据包错误。
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 0));

							break;
						}

					} else {
						// 未接收到该条数据。

						if ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_时间戳_3.getTime()) < 0) {

							// 历史数据。（T还未发送，R就接收到数据了）
							continue;
						}

						this.listEveryPacketLossRate.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), "NA", 0));

						while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
							Str_AIR_T = null;
							Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

							// 判断是否重启。
							if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

								// 空两行。
								// System.out.println();
								// System.out.println();
								if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
									String[] t = lineTxt_br_AIR_T.split(this.str_split);
									reboot_time = t[3];
									reboot = true;
									break;
								} else {
									// 无数据退出。
									break;
								}

							}

							// 判断发送方的CRC标识。
							if (!str_CRC_5.equals(Str_AIR_T[5])) {
								continue;
							}
							if (!this.t_TimeGap(Integer.parseInt(Str_AIR_T[4]))) {
								// 不符合条件就跨过这条数据。
								continue;
							}

							AIR_T_PDU序列号_2 = Str_AIR_T[2];
							AIR_T_时间戳_3 = this.format.parse(Str_AIR_T[3]);

							if (Str_GND_R[2].equals(AIR_T_PDU序列号_2)) {
								// 可能会再次出现T后发，R先收的数据异常情况。
								break;
							} else {
								// 继续丢包。

								this.listEveryPacketLossRate
										.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), "NA", 0));
							}
						}

						if (lineTxt_br_AIR_T == null) {
							// T文件无数据了，要退出。
							break;
						}
						if (reboot) {
							break;
						}

						// 再次判断是否超时或数据包错误。
						// 再次判断是否超时或数据包错误。

						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_时间戳_3.getTime()) <= t_超时值)) {
							// 成功接收到数据。
							// this.AIR_T_count++;
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 1));

							break;
						} else {
							// 超时或数据包错误。

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 0));

							break;
						}

					}
				} else {
					// 如果不是特定机型发过来的数据，直接跳过这一行数据。
					continue;
				}

			}
		}
		br_AIR_T.close();
		br_GND_R.close();

	}

	public void Calculation_AIR_to_GND(File readPath_AIR_T, File readPath_GND_R, String AIR_T_源地址_1, int t_超时值)
			throws IOException, ParseException {

		boolean reboot = false;
		String reboot_time = "";
		String str_CRC_5 = "正确";
		String AIR_T_PDU序列号_2;
		Date AIR_T_时间戳_3;
		this.listEveryPacketLossRate.clear();

		BufferedReader br_AIR_T = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_AIR_T), "gbk"));
		BufferedReader br_GND_R = new BufferedReader(new InputStreamReader(new FileInputStream(readPath_GND_R), "gbk"));

		// 处理无用的表头。
		String lineTxt_br_AIR_T = br_AIR_T.readLine();

		while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
			String[] Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

			// 判断是否重启。
			if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

				// System.out.println("重启了！！！");
				// 空两行。
				// System.out.println();
				// System.out.println();
				if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
					String[] t = lineTxt_br_AIR_T.split(this.str_split);
					reboot_time = t[3];
				} else {
					// 无数据退出。
					break;
				}
				reboot = true;
				continue;
			}

			// 判断发送方的CRC标识。
			if (!str_CRC_5.equals(Str_AIR_T[5])) {
				continue;
			}
			// if (!this.t_时隙号(Integer.parseInt(Str_AIR_T[4]))) {
			// // 不符合条件就跨过这条数据。
			// continue;
			// }

			AIR_T_PDU序列号_2 = Str_AIR_T[2];
			AIR_T_时间戳_3 = this.format.parse(Str_AIR_T[3]);
			String lineTxt_br_GND_R = null;
			while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
				String[] Str_GND_R = lineTxt_br_GND_R.split(this.str_split);
				// 先判断“源地址”，再判断“PDU序列号”，最后判断“时间戳”与“CRC标识”

				// 重启时间判断。
				if (reboot) {
					if ((this.format.parse(Str_GND_R[3]).getTime() - this.format.parse(reboot_time).getTime()) <= 0) {
						while (((lineTxt_br_GND_R = br_GND_R.readLine()) != null)) {
							String[] t = lineTxt_br_GND_R.split(this.str_split);
							// 获取重启之后的数据。
							if ((this.format.parse(t[3]).getTime() - this.format.parse(reboot_time).getTime()) > 0) {
								reboot = false;
								break;
							}
						}
						// R无数据退出。
						if (lineTxt_br_GND_R == null) {
							break;
						}
					}
				}

				if (AIR_T_源地址_1.equals(Str_GND_R[1])) {

					// T接收数据也要校验“时隙号”！！！
					// if (!this.t_时隙号(Integer.parseInt(Str_GND_R[4]))) {
					// // 不符合条件就跨过这条数据。
					// continue;
					// }

					if (AIR_T_PDU序列号_2.equals(Str_GND_R[2])) {
						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_时间戳_3.getTime()) <= t_超时值)) {
							// 成功接收到数据。

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 1));
							// this.writer未丢包(Str_GND_R);
							break;
						} else {
							// 超时或数据包错误。

							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 0));

							break;
						}

					} else {
						// 未接收到该条数据。

						if ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_时间戳_3.getTime()) < 0) {

							// 历史数据。（T还未发送，R就接收到数据了）
							continue;
						}

						this.listEveryPacketLossRate.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), "NA", 0));

						while ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
							Str_AIR_T = null;
							Str_AIR_T = lineTxt_br_AIR_T.split(this.str_split);

							// 判断是否重启。
							if (this.hashMap.get(lineTxt_br_AIR_T) != null) {

								// 空两行。
								// System.out.println();
								// System.out.println();
								if ((lineTxt_br_AIR_T = br_AIR_T.readLine()) != null) {
									String[] t = lineTxt_br_AIR_T.split(this.str_split);
									reboot_time = t[3];
									reboot = true;
									break;
								} else {
									// 无数据退出。
									break;
								}

							}

							// 判断发送方的CRC标识。
							if (!str_CRC_5.equals(Str_AIR_T[5])) {
								continue;
							}
							// if (!this.t_时隙号(Integer.parseInt(Str_AIR_T[4])))
							// {
							// // 不符合条件就跨过这条数据。
							// continue;
							// }

							AIR_T_PDU序列号_2 = Str_AIR_T[2];
							AIR_T_时间戳_3 = this.format.parse(Str_AIR_T[3]);

							if (Str_GND_R[2].equals(AIR_T_PDU序列号_2)) {
								// 可能会再次出现T后发，R先收的数据异常情况。
								break;
							} else {
								// 继续丢包。

								this.listEveryPacketLossRate
										.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), "NA", 0));

							}
						}

						if (lineTxt_br_AIR_T == null) {
							// T文件无数据了，要退出。

							break;
						}
						if (reboot) {
							break;
						}

						// 再次判断是否超时或数据包错误。
						// 再次判断是否超时或数据包错误。

						if (str_CRC_5.equals(Str_GND_R[5])
								&& ((this.format.parse(Str_GND_R[3]).getTime() - AIR_T_时间戳_3.getTime()) <= t_超时值)) {
							// 成功接收到数据。
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 1));
							//
							// this.writer未丢包(Str_GND_R);
							break;
						} else {
							// 超时或数据包错误。
							this.listEveryPacketLossRate
									.add(new everyPacketLossRate(format.format(AIR_T_时间戳_3), Str_GND_R[3], 0));

							break;
						}

					}
				} else {
					// 如果不是特定机型发过来的数据，直接跳过这一行数据。
					continue;
				}

			}
		}
		br_AIR_T.close();
		br_GND_R.close();

	}

	public void calculateLineChart(String date_time, String big_batch, String kind_AIR_GND, int batch, int num_T,
			int num_R) throws ParseException {

		// 无数据，直接退出 （某机型没有接受到另一机型的数据），就不处理了。
		if (this.listEveryPacketLossRate.size() == 0) {
			return;
		}

		Date time_S;
		Date time_E;
		// 存储分段的时间点。
		List<Date> list_Date = new ArrayList<>();

		time_S = this.format.parse(this.listEveryPacketLossRate.get(0).time_T);
		time_E = this.format.parse(this.listEveryPacketLossRate.get(this.listEveryPacketLossRate.size() - 1).time_T);

		long duan = time_E.getTime() - time_S.getTime();
		long duan_step = duan / (this.lineChart);

		// 计算时间，应该加1秒。
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
					// 是本时间段的。
					sum_R = sum_R + this.listEveryPacketLossRate.get(j).packetLossRateState;
					sum_T++;
				} else {
					// 是下一个时间段。
					this.list_LineChart.add(new LineChart(date_time, big_batch, kind_AIR_GND, batch, num_T, num_R,
							sum_T, sum_R, new Date(list_Date.get(i).getTime() - 1000)));
					// 获取本行的丢包率信息。
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
		// 判断二级目录是否合法。
		if (DateName.length != 1) {
			System.out.println("该目录为空或有多个日期文件！");
			return;
		} else {
			String time = DateName[0].getName();
			if (time.length() != 8) {
				System.out.println("无法找到合法的日期文件！");
				return;
			} else {
				// 获取年、月、日时间戳。
				dataPDU = time.substring(0, 4) + stitching + time.substring(4, 6) + stitching + time.substring(6, 8);
			}
		}

		File[] files_big_batch = DateName[0].listFiles();
		// files_big_batch是各个大批次目录。
		for (int i = 0; i < files_big_batch.length; i++) {

			// kind_AIR_GND是AIR、GND文件夹。
			File[] kind_AIR_GND = files_big_batch[i].listFiles();

			for (int j = 0; j < kind_AIR_GND.length; j++) {
				// 转化为小写的文件名。
				String str_kind = kind_AIR_GND[j].getName().toLowerCase();
				// 按照“_"分割文件名。
				String[] str_kind_s = str_kind.split(this.str_split2);
				if (str_kind_s.length > 1) {
					// 一定是GND_*
					// 获取GND的T与R文件。
					File[] T_R_rate_s = kind_AIR_GND[j].listFiles();

					String[] num_GND = T_R_rate_s[0].getName().split(this.str_split2);
					String str_T = null;
					String str_R = T_R_rate_s[0].getPath();

					this.list_big_batch.add(new T_and_R(Integer.parseInt(num_GND[0]), "GND", str_T, str_R,
							Integer.parseInt(str_kind_s[1]), files_big_batch[i].getName(), dataPDU));

				} else {
					// 是AIR或GND文件夹。
					if (this.AIR_toLowerCase.equals(kind_AIR_GND[j].getName().toLowerCase())) {
						// 是AIR文件夹
						// AIR_file_s是AIR文件夹下的飞机编号文件夹。
						File[] AIR_file_s = kind_AIR_GND[j].listFiles();

						// 访问所有的AIR文件夹
						for (int k = 0; k < AIR_file_s.length; k++) {
							// 下面判断AIR的小批次。
							// 批次号与文件名。
							String[] str_name = AIR_file_s[k].getName().split(this.str_split2);
							// T_R_rate_s是T与R文件。
							File[] T_R_rate_s = AIR_file_s[k].listFiles();
							String str_T = null;
							String str_R = null;
							if (str_name.length > 1) {
								// 有小批次。
								for (int ii = 0; ii < T_R_rate_s.length; ii++) {
									// 转化为大写字母，并用"_"分词 例：101_R.rate
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
									// 转化为大写字母，并用"_"分词 例：101_R.rate
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
						// 是GND文件夹。
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
						// 是空机发送、小班次相同、机器编号不一样。
						if (this.AIR_toLowerCase.equals(kind_AIR_GND02.toLowerCase())) {
							// 空对空。

							// 1、找T（发送文件）的重启点（并检查异常不连续的PDU号）。
							this.findReboot(new File(T_path01), this.MAX_int_PDU);

							// 2、初始化init_hashMap。
							this.init_hashMap();

							// 3、输入时隙号码表。
							this.t_select("big");

							//
							// 4、计算丢包率并生成csv文件。

							this.Calculation_AIR_to_AIR(new File(T_path01), new File(R_path02), ("" + num01),
									this.t_timeOver);

							// 5、计算各时间段的丢包率。

							this.calculateLineChart(date_time01, big_batch01, "AIR_to_AIR", batch01, num01, num02);

						} else {
							// 空对地。
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

	//只输出SQL语句。
	public void mainFunction(File readPath, String sql_table_name) throws IOException, ParseException {
		// 文件夹路径、mysql表名。
		this.readPathFile(readPath);
		this.CartesianProduct();
		this.putLineChartInformation(sql_table_name);
	}

//	向mysql中插入数据。
	public void mainFunction(File readPath, String sql_table_name, String DB_URL, String USER, String PASS)
			throws IOException, ParseException, ClassNotFoundException, SQLException {
		// 文件夹路径、mysql表名、mysql地址、mysql用户名、mysql密码。
		this.readPathFile(readPath);
		this.CartesianProduct();
		this.putIntoMysql(sql_table_name, DB_URL, USER, PASS);
	}

	public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException, SQLException {
		packetLossRate a = new packetLossRate();
		// a.mainFunction(new// File("D:/PDU"),"packetLossRate");
		a.mainFunction(new File("D:/PDU"), "packetLossRate","jdbc:mysql://192.168.1.120:3306/test", "root", "");

		System.out.println("运行完成！");

	}

}
