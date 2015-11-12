package org.androidpn.client;

import org.litepal.crud.DataSupport;

/**
 * 实体类，自动映射成litepal数据库的一张表
 * litepal默认给主键id
 * 记得在litepal配置       <mapping class="org.androidpn.client.NotificationHistory"></mapping>   

 * @author chenjiacheng
 *
 */
public class NotificationHistory extends DataSupport {

	private String apiKey;

	private String title;

	private String message;

	private String uri;
	
	private String imageUrl;

	private String time;
	
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	
}
