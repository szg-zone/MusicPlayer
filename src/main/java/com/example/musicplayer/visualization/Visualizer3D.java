package com.example.musicplayer.visualization;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

/**
 * Renders frequency data as animated cubes in a JavaFX 3D SubScene.
 */
public class Visualizer3D extends Region {
    private final SubScene subScene;
    private final Box[] cubes;

    public Visualizer3D(int bands) {
        Group worldRoot = new Group();
        Group cubesGroup = new Group();
        cubes = new Box[bands];

        for (int i = 0; i < bands; i++) {
            Box cube = new Box(7, 16, 10);
            cube.setTranslateX((i - bands / 2.0) * 9.0);
            cube.setTranslateY(170);

            cube.setMaterial(new PhongMaterial(Color.hsb((double) i / bands * 280, 0.85, 0.92)));
            cubes[i] = cube;
            cubesGroup.getChildren().add(cube);
        }

        worldRoot.getChildren().add(cubesGroup);
        worldRoot.getTransforms().addAll(
                new Rotate(-17, Rotate.X_AXIS),
                new Rotate(-20, Rotate.Y_AXIS)
        );

        AmbientLight ambient = new AmbientLight(Color.rgb(140, 140, 150));
        PointLight key = new PointLight(Color.WHITE);
        key.setTranslateX(-450);
        key.setTranslateY(-240);
        key.setTranslateZ(-350);
        worldRoot.getChildren().addAll(ambient, key);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-860);
        camera.setTranslateY(-130);
        camera.setNearClip(0.1);
        camera.setFarClip(4000);

        subScene = new SubScene(worldRoot, 900, 520, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.rgb(8, 10, 18));
        subScene.setCamera(camera);

        getChildren().add(subScene);
    }

    /**
     * Converts each frequency magnitude to cube height and depth scale.
     */
    public void render(float[] magnitudes, int thresholdDb) {
        int count = Math.min(magnitudes.length, cubes.length);
        for (int i = 0; i < count; i++) {
            double normalized = SpectrumMath.normalizedMagnitude(magnitudes[i], thresholdDb);
            double height = SpectrumMath.scale(normalized, 12.0, 290.0);

            cubes[i].setHeight(height);
            cubes[i].setTranslateY(185 - (height / 2.0));
            cubes[i].setScaleZ(SpectrumMath.scale(normalized, 0.8, 2.6));
        }
    }

    @Override
    protected void layoutChildren() {
        subScene.setWidth(getWidth());
        subScene.setHeight(getHeight());
    }
}
