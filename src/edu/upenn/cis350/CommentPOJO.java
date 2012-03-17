package edu.upenn.cis350;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentPOJO implements Parcelable {
	private String commentText;
	private String author; // should this be authorID?
	private String timestamp;
	
	// Do I need this?
	public static final Parcelable.Creator<CommentPOJO> CREATOR = new Parcelable.Creator<CommentPOJO>() {
        public CommentPOJO createFromParcel(Parcel in) {
            return new CommentPOJO(in);
        }

        public CommentPOJO[] newArray(int size) {
            return new CommentPOJO[size];
        }
    };
    
    public CommentPOJO() {
    	
    }
    
	public CommentPOJO(Parcel in) {
		commentText = in.readString();
		author = in.readString();
		timestamp = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return hashCode();
	}
	
	public String getText() {
		return commentText;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setText(String msg) {
		commentText = msg;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setTimestamp(String time) {
		timestamp = time;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(commentText);
		dest.writeString(author);
		dest.writeString(timestamp);
	}

}
