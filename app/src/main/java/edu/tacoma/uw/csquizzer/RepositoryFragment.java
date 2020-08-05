package edu.tacoma.uw.csquizzer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The RepositoryFragment is placed in MainActivity.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class RepositoryFragment extends Fragment {
    /**
     * Render components to GUI
     * @param inflater a class used to instantiate layout XML file into its corresponding view objects
     * @param container a special view that can contain other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     * @return view
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repository, container, false);
    }
}