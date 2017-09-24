package eu.davidea.starterapp.ui.common;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * {@link ActionMode.Callback} implementation that does nothing by default
 */
public abstract class SimpleActionModeCallback implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

}