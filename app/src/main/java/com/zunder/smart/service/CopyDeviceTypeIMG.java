package com.zunder.smart.service;

import com.zunder.smart.setting.ProjectUtils;

public class CopyDeviceTypeIMG {
	static String pImage[] = { "mc_on",
			"mc_off", "key_on_1",
			"key_on_2", "key_on_3",
			"key_off_1", "key_off_2",
			"key_off_3", "k_1527","key_sensor","sl_img","sl_img","sl_img","sl_img","sl_img","sl_img","mc_16","mc_17","mc_18"};
	static String airImage[] = { "pic_kz_sd_on_off",
			"pic_kz_sd_on_18", "pic_kz_sd_on_19",
			"pic_kz_sd_on_20", "pic_kz_sd_on_21",
			"pic_kz_sd_on_22", "pic_kz_sd_on_23",
			"pic_kz_sd_on_24", "pic_kz_sd_on_25",
			"pic_kz_sd_on_26", "pic_kz_sd_on_27",
			"pic_kz_sd_on_28", "pic_kz_sd_on_29",
			"pic_kz_sd_on_30" };

	public static String initDeviceCtroll(String type, int action, int io) {
		String imgID = "";
		int ariortvType = Integer.valueOf(type, 16);
		if ((ariortvType >= 48) && (ariortvType < 80)) {
			imgID = airImage[action];
		} else if ((ariortvType > 79) && (ariortvType < 101)) {
			imgID = (action > 0) ? "pic_ts_rsqon"
					: "pic_ts_rsqoff";
		} else if (ariortvType >= 102 && ariortvType <= 111) {
			imgID = (action > 0) ? "project_on"
					: "project_off";
		} else {
			switch (ariortvType) {
				case 0: // 1DI1DO
					break;
				case 11:
					imgID = (action > 0) ? "pic_kz_cz_on"
							: "pic_kz_cz_off";
					break;
				case 4:
					imgID ="dzs";
					break;
				case 1:
				case 3:
				case 6:
				case 39:
					imgID = (action > 0) ? "device_electric_light1_on"
							: "device_electric_light1_off";
					break;
				case 7:
					if (action == 0) {
						imgID = "pic_kz_cf_off";
						// imgID = "pic_kz_cfs;
					} else if (action == 1) {
						imgID = "pic_kz_bf_on";
						// imgID = "pic_kz_bfs;
					} else if (action == 2) {
						imgID = "pic_kz_cfs";
					} else if (action == 3) {
						imgID = "pic_kz_bfs";
					}
					break;
				case 13:
					imgID = (action > 0) ? "dimmer_on"
							: "dimmer_off";
					break;
				case 8:
					imgID = (action > 0) ? "timeswitch_on"
							: "timeswitch_off";
					break;
				case 2:
					if (ProjectUtils.getRootPath().getRootVersion() == 0) {
						imgID = (action > 0) ? "device_electric_light1_on"
								: "device_electric_light1_off";
					} else {
						switch (io) {
							case 0:
								imgID = (action > 0) ? "eq_curtain_bg_on"
										: "eq_curtain_bg_off";
								break;
							case 1:
								imgID = (action > 0) ? "eq_curtain_bg_on_1"
										: "eq_curtain_bg_off_1";
								break;
							case 2:
								imgID = (action > 0) ? "eq_curtain_bg_on_2"
										: "eq_curtain_bg_off_2";
								break;
							case 3:
								imgID = (action > 0) ? "eq_curtain_bg_on_3"
										: "eq_curtain_bg_off_3";
								break;
							case 4:
								imgID = (action > 0) ? "eq_curtain_bg_on_4"
										: "eq_curtain_bg_off_4";
								break;
							default:
								imgID = (action > 0) ? "eq_curtain_bg_on"
										: "eq_curtain_bg_off";
								break;
						}
					}
					break;
				case 22:
					imgID = "people";
					break;
				case 12:
					imgID = (action > 0) ? "eq_curtain_bg_on"
							: "eq_curtain_bg_off";
					break;
				case 163:
					imgID = (action > 0) ? "pic_dcd_on"
							: "pic_dcd_off";
					break;
				case 5:
					imgID = "red_fra_img";
					break;
				case 9:
					break;
				case 33:
					imgID = (action > 0) ? "pic_music_on"
							: "pic_music_off";
					break;
				case 34:
					break;
				case 200:
					break;
				case 101:
					imgID ="tv_listing";
					break;
				case 201:
				case 255:
					break;
				case 173:
					imgID =  "right_add";
					break;
			}
		}
		return "http://112.74.64.82:99/Images/devicestate/"+imgID+".png";
	}
}
