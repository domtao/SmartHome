package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.Room;

public class RoomFactory {

	private volatile static RoomFactory install;
	public static RoomFactory getInstance() {
		if (null == install) {
			install = new RoomFactory();
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}
		return install;
	}
	private static  List<Room> list = new ArrayList<Room>();



	public  List<Room> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}
		return list;
	}

	public  Room getRoomByName(String roomName) {
		Room room=null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}
        for (Room _room : list)
        {
            if (_room.getRoomName().equals(roomName)) {
                room=_room;
                break;
            }
        }
		return room;
	}

	public  List<Room> getRoom(int homePageDisplays) {
		List<Room> resultList = new ArrayList<Room>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}
		for (Room room : list) {
			if (homePageDisplays == -1) {
				resultList.add(room);
			} else {
				if (room.getIsShow() == homePageDisplays) {
					resultList.add(room);
				}
			}
		}
		return resultList;
	}
	public int  getRoomControl() {
		int result=0;
		for (Room room : list) {
			if (room.getIsShow() == 2) {
				result=1;
				break;
			}
		}
		return result;
	}
	public  List<String> getRoomName(int isShow) {
		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}

		for (Room room : list) {
			if(room.getIsShow()==isShow) {
				resultList.add(room.getRoomName());
			}
		}
		return resultList;
	}

	public  Room getArceById(int Id) {
		Room room = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}

		for (Room _room : list) {
			if (_room.getId() == Id) {
				room = _room;
				break;
			}

		}
		return room;
	}
	public  String getRoomById(int Id) {
		String roomName="客厅";
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase().getRoom();
		}

		for (Room _room : list) {
			if (_room.getId() == Id) {
			roomName=_room.getRoomName();
				break;
			}

		}
		return roomName;
	}

	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getRoom();
	}

	public static List<Room> getSelect() {
		List<Room> resultList = new ArrayList<Room>();

		Room room = new Room();
		room.setRoomName(MyApplication.getInstance().getString(R.string.help_all));
		room.setPrimary_Key("FF");
		resultList.add(room);

		Room arce1 = new Room();
		arce1.setRoomName(MyApplication.getInstance().getString(R.string.switch_code));
		arce1.setPrimary_Key("00");
		resultList.add(arce1);

		Room arce2 = new Room();
		arce2.setRoomName(MyApplication.getInstance().getString(R.string.dimmer_code));
		arce2.setPrimary_Key("03");
		resultList.add(arce2);

		Room arce3 = new Room();
		arce3.setRoomName(MyApplication.getInstance().getString(R.string.ml_code));
		arce3.setPrimary_Key("04");
		resultList.add(arce3);

		Room arce4 = new Room();
		arce4.setRoomName(MyApplication.getInstance().getString(R.string.control_code));
		arce4.setPrimary_Key("19");
		resultList.add(arce4);

		Room arce5 = new Room();
		arce5.setRoomName(MyApplication.getInstance().getString(R.string.sence_code));
		arce5.setPrimary_Key("32");
		resultList.add(arce5);

		return resultList;
	}

}
