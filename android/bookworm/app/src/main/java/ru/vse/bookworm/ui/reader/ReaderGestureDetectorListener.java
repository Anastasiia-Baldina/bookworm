package ru.vse.bookworm.ui.reader;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class ReaderGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }
        boolean res = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
