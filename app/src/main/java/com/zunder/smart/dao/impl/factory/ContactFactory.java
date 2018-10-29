package com.zunder.smart.dao.impl.factory;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.ContactIngfo;
import com.zunder.smart.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class ContactFactory {
	List<ContactIngfo> list=new ArrayList<ContactIngfo>();
	private static final String[] PHONES_PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID,ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
	private volatile static ContactFactory install;
	public static ContactFactory getInstance() {
		if (null == install) {
			install = new ContactFactory();
				}
		return install;
	}

	private List<ContactIngfo> getContacts() {
		//联系人的Uri，也就是content://com.android.contacts/contacts
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		//指定获取_id和display_name两列数据，display_name即为姓名
		String[] projection = new String[] {
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME
		};
		//根据Uri查询相应的ContentProvider，cursor为获取到的数据集
		Cursor cursor = MyApplication.getInstance().getContentResolver().query(uri, projection, null, null, null);

		int i = 0;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				ContactIngfo contactIngfo=new ContactIngfo();
				Long id = cursor.getLong(0);
				//获取姓名
				String name = cursor.getString(1);
				//指定获取NUMBER这一列数据
				String[] phoneProjection = new String[] {
						ContactsContract.CommonDataKinds.Phone.NUMBER
				};
				contactIngfo.setId(id);
				contactIngfo.setName(name);
				//根据联系人的ID获取此人的电话号码
				Cursor phonesCusor = MyApplication.getInstance().getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						phoneProjection,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
						null,
						null);

				//因为每个联系人可能有多个电话号码，所以需要遍历
				if (phonesCusor != null && phonesCusor.moveToFirst()) {
					String result="";
					do {
						String num = phonesCusor.getString(0);
						result +=num+",";
//						contactIngfo.setNumber(num);
					}while (phonesCusor.moveToNext());

					contactIngfo.setNumber(result);
				}
				i++;
				list.add(contactIngfo);

			} while (cursor.moveToNext());
		}
		return list;
	}

	public String getContactName(String num){
		String result="陌生人来电";
		if(list.size()==0){
			getContacts();
		}

		for (int i=0;i<list.size();i++){
			ContactIngfo contactIngfo=list.get(i);
			if(contactIngfo.getNumber().contains(num)){
				result=contactIngfo.getName();
				break;
			}
		}
		return result;
	}
}
