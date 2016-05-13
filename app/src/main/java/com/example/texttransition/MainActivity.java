package com.example.texttransition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int DURATION = 3000;

    private Button transitionTrigger;
    private ViewGroup sceneRoot;
    private boolean isSceneA = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        findSceneViews(sceneRoot);

        initialize();

    }

    private void initialize(){
        //Create the scenes
        final Scene sceneA = Scene.getSceneForLayout(sceneRoot, R.layout.activity_main_scene_a, this);
        final Scene sceneB = Scene.getSceneForLayout(sceneRoot, R.layout.activity_main_scene_b, this);
        //Create the Transition
        final Transition transition = new ChangeBounds();
        transition.setDuration(DURATION);
        //Create an onClick object so it can be reset to the button in the new Scene at the onTransitionEnd.
        final View.OnClickListener triggerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSceneA) TransitionManager.go(sceneB, transition);
                else TransitionManager.go(sceneA, transition);
            }
        };
        //Set the listener for the first time.
        transitionTrigger.setOnClickListener(triggerListener);
        //Transition listener
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}
            @Override
            public void onTransitionEnd(Transition transition) {
                //Reverse its value.
                isSceneA = !isSceneA;
                //All the views in both scenes have the same ids, we just need to find them again
                //within the new scene's root View.
                if (isSceneA) findSceneViews(sceneA.getSceneRoot());
                else findSceneViews(sceneB.getSceneRoot());
                //Reset the listener to the button in the new scene, previous button is not available now.
                transitionTrigger.setOnClickListener(triggerListener);
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }



    private void findSceneViews(ViewGroup scene){
        transitionTrigger = (Button) scene.findViewById(R.id.transition_trigger);
    }

}
