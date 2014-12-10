package com.example.paperdemo;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

public class DummyShadowBuilder extends View.DragShadowBuilder {

    @Override
    public void onDrawShadow(Canvas canvas) {
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point touchPoint) {
    }
}