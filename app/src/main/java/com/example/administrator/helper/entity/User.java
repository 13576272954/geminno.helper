package com.example.administrator.helper.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.sql.Timestamp;

/**
 * 实体类
 * 用户
 * @author Administrator
 *
 */
public class User implements Parcelable {
	private Integer id;//用户id
	private String name;//用户账号
	private String password;//用户密码
	private Integer payPassword;//支付密码
	private String sex;//性别
	private Integer age;//年龄
	private Integer credit;//用户信誉
	private  Integer rank;//信誉等级
	private Integer points;//积分
	private String school;//学校
	private String sign;//签名
	private Double balance;//余额
	private Timestamp time;//创建日期
	private String phoneNumber;//电话号码
	private String image;//图片
	private float distance;
	public User(String name, String sex, Integer age, String image) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.image = image;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", payPassword=" + payPassword +
				", sex='" + sex + '\'' +
				", age=" + age +
				", credit=" + credit +
				", rank=" + rank +
				", points=" + points +
				", school='" + school + '\'' +
				", sign='" + sign + '\'' +
				", balance=" + balance +
				", time=" + time +
				", phoneNumber='" + phoneNumber + '\'' +
				", image='" + image + '\'' +
				", distance=" + distance +
				'}';
	}
	public User(Integer id, String name, String sex, Integer age, String image, float distance) {
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.image = image;
		this.distance = distance;
	}

	public float calDistance(LatLng userpoint , LatLng minepoint ){
		DistanceUtil distance=new DistanceUtil();
		float newDistance = (float) distance.getDistance(userpoint,minepoint);

		return newDistance;
	}
	public float getDistance() {

		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public User(int id, String name, String password, Integer payPassword, String sex, Integer age,
				Integer credit, Integer rank, Integer points, String school, String sign, Double balance,
				Timestamp time, String phoneNumber, String image) {
		// TODO Auto-generated constructor stub
		super();
		this.id=id;
		this.name = name;
		this.password = password;
		this.payPassword = payPassword;
		this.sex = sex;
		this.age = age;
		this.credit = credit;
		this.rank = rank;
		this.points = points;
		this.school = school;
		this.sign = sign;
		this.balance = balance;
		this.time = time;
		this.phoneNumber = phoneNumber;
		this.image = image;
	}



	public User(Integer id, String name, String password, Integer payPassword, String sex, Integer age, Integer credit,
				Integer rank, Integer points, String school, String sign, Double balance, String phoneNumber,
				String image) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.payPassword = payPassword;
		this.sex = sex;
		this.age = age;
		this.credit = credit;
		this.rank = rank;
		this.points = points;
		this.school = school;
		this.sign = sign;
		this.balance = balance;
		this.phoneNumber = phoneNumber;
		this.image = image;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Integer getPayPassword() {
		return payPassword;
	}


	public void setPayPassword(Integer payPassword) {
		this.payPassword = payPassword;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Integer getCredit() {
		return credit;
	}


	public void setCredit(Integer credit) {
		this.credit = credit;
	}


	public Integer getPoints() {
		return points;
	}


	public void setPoints(Integer points) {
		this.points = points;
	}


	public String getSchool() {
		return school;
	}


	public void setSchool(String school) {
		this.school = school;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public Double getBalance() {
		return balance;
	}


	public void setBalance(Double balance) {
		this.balance = balance;
	}


	public Timestamp getTime() {
		return time;
	}


	public void setCreateDate(Timestamp time) {
		this.time = time;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public Integer getId() {
		return id;
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}




	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public User(Integer id, String name, String password, Integer payPassword, String sex, Integer age, Integer credit, Integer rank, Integer points, String school, String sign, Double balance, Timestamp time, String phoneNumber, String image, float distance) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.payPassword = payPassword;
		this.sex = sex;
		this.age = age;
		this.credit = credit;
		this.rank = rank;
		this.points = points;
		this.school = school;
		this.sign = sign;
		this.balance = balance;
		this.time = time;
		this.phoneNumber = phoneNumber;
		this.image = image;
		this.distance = distance;
	}

	public User() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.name);
		dest.writeString(this.password);
		dest.writeValue(this.payPassword);
		dest.writeString(this.sex);
		dest.writeValue(this.age);
		dest.writeValue(this.credit);
		dest.writeValue(this.rank);
		dest.writeValue(this.points);
		dest.writeString(this.school);
		dest.writeString(this.sign);
		dest.writeValue(this.balance);
		dest.writeSerializable(this.time);
		dest.writeString(this.phoneNumber);
		dest.writeString(this.image);
	}

	protected User(Parcel in) {
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.name = in.readString();
		this.password = in.readString();
		this.payPassword = (Integer) in.readValue(Integer.class.getClassLoader());
		this.sex = in.readString();
		this.age = (Integer) in.readValue(Integer.class.getClassLoader());
		this.credit = (Integer) in.readValue(Integer.class.getClassLoader());
		this.rank = (Integer) in.readValue(Integer.class.getClassLoader());
		this.points = (Integer) in.readValue(Integer.class.getClassLoader());
		this.school = in.readString();
		this.sign = in.readString();
		this.balance = (Double) in.readValue(Double.class.getClassLoader());
		this.time = (Timestamp) in.readSerializable();
		this.phoneNumber = in.readString();
		this.image = in.readString();
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
