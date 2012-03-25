package edu.upenn.cis350;

import android.os.Parcel;
import android.os.Parcelable;

/* Plain Old Java Object to represent a comment.
 * This is passed throughout the app rather than making
 * frequent database queries
 */
public class CommentPOJO implements Parcelable {
	private String commentText;
	private String author; // should this be authorID?
	private String timestamp;
	private int messageId;
	
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
    
    // Necessary constructor b/c of "Parcelable" interface
	public CommentPOJO(Parcel in) {
		messageId = in.readInt();
		commentText = in.readString();
		author = in.readString();
		timestamp = in.readString();
	}
	
	@Override
	public int describeContents() {
		return hashCode();
	}
	
	public int getMessageId() {
		return messageId;
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
	
	public void setMessageId(int id) {
		messageId = id;
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
	// flattens fields
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(messageId);
		dest.writeString(commentText);
		dest.writeString(author);
		dest.writeString(timestamp);
	}
	
	// TODO: Change to builder interface

//	public class CommentPOJOBuilder {
//		private CommentPOJO comment = null;
//		
//		public CommentPOJOBuilder() {
//			comment = new CommentPOJO();
//		}
//		
//		public CommentPOJOBuilder setText(String msg) {
//			comment.setText(msg);
//			return this;
//		}
//		
//		public CommentPOJOBuilder setAuthor(String author) {
//			comment.setAuthor(author);
//			return this;
//		}
//		
//		public CommentPOJOBuilder setTimestamp(String time) {
//			comment.setTimestamp(time);
//			return this;
//		}
//		
//		public CommentPOJO build() {
//			if (comment.getText() != null &&
//					comment.getAuthor() != null &&
//					comment.getTimestamp() != null) {
//				return comment;
//			} else {
//				return null;
//			}
//		}
//	}

}
