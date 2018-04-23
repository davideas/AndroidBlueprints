package eu.davidea.blueapp.ui.items;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import eu.davidea.blueapp.viewmodels.message.Message;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHolder;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * @author Davide
 * @since 23/09/2017
 */
public class HolderMessageItem extends AbstractFlexibleItem<HolderMessageItem.MessageViewHolder> implements IHolder<Message> {
    
    private Message model;

    public HolderMessageItem(@NonNull Message menuItem) {
        this.model = menuItem;
    }

    /**
     * @return the model object {@link Message}
     */
    @Override
    public Message getModel() {
        return model;
    }

    public void setModel(Message model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof HolderMessageItem && model.equals(((HolderMessageItem) o).getModel());
    }

    @Override
    public int hashCode() {
        return model != null ? model.hashCode() : 0;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public HolderMessageItem.MessageViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new MessageViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HolderMessageItem.MessageViewHolder holder, int position, List payloads) {
        
    }
    
    static class MessageViewHolder extends FlexibleViewHolder {
        
        MessageViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
        }

    }
    
}
