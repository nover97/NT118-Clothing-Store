package app.nover.clothingstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.nover.clothingstore.models.Message;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ADMIN_MESSAGE = 1;
    private static final int VIEW_TYPE_USER_MESSAGE = 0;

    private Context context;
    private ArrayList<Message> messageArrayList;

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        // Sắp xếp danh sách tin nhắn theo thời gian theo thứ tự tăng dần
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_TYPE_ADMIN_MESSAGE) {
            v = LayoutInflater.from(context).inflate(R.layout.item_admin_message, parent, false);
            return new AdminMessageViewHolder(v);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);

        if (holder instanceof AdminMessageViewHolder) {
            AdminMessageViewHolder adminViewHolder = (AdminMessageViewHolder) holder;
            adminViewHolder.adminMessageContent.setText(message.getMessageContent());
        } else if (holder instanceof UserMessageViewHolder) {
            UserMessageViewHolder userViewHolder = (UserMessageViewHolder) holder;
            userViewHolder.userMessageContent.setText(message.getMessageContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getWho().equals("admin")) {
            return VIEW_TYPE_USER_MESSAGE; // Tin nhắn của người dùng
        } else {
            return VIEW_TYPE_ADMIN_MESSAGE; // Tin nhắn của quản trị viên
        }
    }

    public static class AdminMessageViewHolder extends RecyclerView.ViewHolder {
        TextView adminMessageContent;

        public AdminMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            adminMessageContent = itemView.findViewById(R.id.tvadmessage);
        }
    }

    public static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageContent;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageContent = itemView.findViewById(R.id.tvusermessage);
        }
    }

    public void sortDataByTime() {
        Collections.sort(messageArrayList, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getTime().compareTo(message2.getTime());
            }
        });

    }
}
