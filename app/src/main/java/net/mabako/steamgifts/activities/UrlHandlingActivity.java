package net.mabako.steamgifts.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import net.mabako.steamgifts.data.BasicDiscussion;
import net.mabako.steamgifts.data.BasicGiveaway;
import net.mabako.steamgifts.fragments.DiscussionDetailFragment;
import net.mabako.steamgifts.fragments.GiveawayDetailFragment;
import net.mabako.steamgifts.fragments.UserDetailFragment;

import java.util.List;

/**
 * Handle all URL related shenanigans.
 */
public class UrlHandlingActivity extends CommonActivity {
    private static final String TAG = UrlHandlingActivity.class.getSimpleName();

    public static Intent getIntentForUri(Context context, Uri uri) {
        Log.d(TAG, uri.toString());
        if ("www.steamgifts.com".equals(uri.getHost()) || "steamgifts.com".equals(uri.getHost())) {
            Log.d(TAG, "Parsing path segment " + uri.getPath());
            List<String> pathSegments = uri.getPathSegments();

            if (pathSegments.size() == 0 || ("/giveaways/search".equals(uri.getPath()))) {
                // TODO parse query params?
                return new Intent(context, MainActivity.class);
            } else if (pathSegments.size() >= 2) {
                // Can't really do anything reasonable without at least two path segments
                // Giveaways!
                if ("giveaway".equals(pathSegments.get(0))) {
                    String giveawayId = pathSegments.get(1);
                    // Giveaway Ids are always 5 chars long.
                    if (giveawayId.length() == 5) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(GiveawayDetailFragment.ARG_GIVEAWAY, new BasicGiveaway(giveawayId));
                        return intent;
                    }
                } else if ("discussion".equals(pathSegments.get(0))) {
                    String discussionId = pathSegments.get(1);
                    // Discussion Ids are always 5 chars long.
                    if (discussionId.length() == 5) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(DiscussionDetailFragment.ARG_DISCUSSION, new BasicDiscussion(discussionId));
                        return intent;
                    }
                } else if ("user".equals(pathSegments.get(0))) {
                    String user = pathSegments.get(1);

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(UserDetailFragment.ARG_USER, user);
                    return intent;
                }
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start whatever intent we were launching
        Intent intentToStart = getIntentForUri(this, getIntent().getData());
        if (intentToStart != null) {
            startActivity(intentToStart);
        } else {
            // Fallback for opening an unknown url.
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}