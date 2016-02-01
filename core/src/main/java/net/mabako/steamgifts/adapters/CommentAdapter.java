package net.mabako.steamgifts.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.mabako.steamgifts.adapters.viewholder.CommentContextViewHolder;
import net.mabako.steamgifts.adapters.viewholder.CommentViewHolder;
import net.mabako.steamgifts.adapters.viewholder.DiscussionCardViewHolder;
import net.mabako.steamgifts.adapters.viewholder.GiveawayCardViewHolder;
import net.mabako.steamgifts.data.Comment;
import net.mabako.steamgifts.fragments.DiscussionDetailFragment;
import net.mabako.steamgifts.fragments.GiveawayDetailFragment;
import net.mabako.steamgifts.fragments.ListFragment;
import net.mabako.steamgifts.fragments.interfaces.ICommentableFragment;
import net.mabako.steamgifts.fragments.util.DiscussionDetailsCard;
import net.mabako.steamgifts.fragments.util.GiveawayDetailsCard;

import java.util.List;

/**
 * Adapter to hold comments for a giveaway/discussion.
 */
public class CommentAdapter extends EndlessAdapter {
    private static final long serialVersionUID = 5961119226634909060L;

    /**
     * Amount of top-level items on a full comments page.
     */
    private static final int ITEMS_PER_PAGE = 25;

    /**
     * Fragment this all is shown in.
     */
    private transient Fragment fragment;

    public CommentAdapter() {
        this.alternativeEnd = true;
    }

    public void setFragmentValues(ListFragment fragment) {
        setLoadListener(fragment);
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateActualViewHolder(View view, int viewType) {
        if (fragment == null)
            throw new IllegalStateException("Ain't got no fragment");

        if (viewType == Comment.VIEW_LAYOUT) {
            return new CommentViewHolder(view, fragment.getActivity(), (ICommentableFragment) fragment);
        } else if (viewType == GiveawayDetailsCard.VIEW_LAYOUT) {
            return new GiveawayCardViewHolder(view, (GiveawayDetailFragment) fragment);
        } else if (viewType == DiscussionDetailsCard.VIEW_LAYOUT) {
            return new DiscussionCardViewHolder(view, (DiscussionDetailFragment) fragment);
        } else if(viewType == CommentContextViewHolder.VIEW_LAYOUT) {
            return new CommentContextViewHolder(view, fragment.getActivity());
        }
        return null;
    }

    @Override
    public void onBindActualViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h instanceof CommentViewHolder) {
            CommentViewHolder holder = (CommentViewHolder) h;
            Comment comment = (Comment) getItem(position);

            holder.setFrom(comment);
        } else if (h instanceof GiveawayCardViewHolder) {
            GiveawayCardViewHolder holder = (GiveawayCardViewHolder) h;
            GiveawayDetailsCard card = (GiveawayDetailsCard) getItem(position);

            holder.setFrom(card);
        } else if (h instanceof DiscussionCardViewHolder) {
            DiscussionCardViewHolder holder = (DiscussionCardViewHolder) h;
            DiscussionDetailsCard card = (DiscussionDetailsCard) getItem(position);

            holder.setFrom(card);
        } else if(h instanceof CommentContextViewHolder) {
            CommentContextViewHolder holder = (CommentContextViewHolder) h;
            CommentContextViewHolder.SerializableHolder info = (CommentContextViewHolder.SerializableHolder) getItem(position);

            holder.setFrom(info);
        }
    }

    @Override
    protected boolean hasEnoughItems(List<IEndlessAdaptable> items) {
        if (items.size() < ITEMS_PER_PAGE)
            return false;

        int rootLevelComments = 0;
        for (IEndlessAdaptable adaptable : items)
            if (adaptable instanceof Comment && ((Comment) adaptable).getDepth() == 0)
                ++rootLevelComments;

        return rootLevelComments == ITEMS_PER_PAGE;
    }
}
