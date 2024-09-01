package com.epimorphismmc.monomorphism.client.render;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static com.gregtechceu.gtceu.utils.GTUtil.DIRECTIONS;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.SOUTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

@OnlyIn(Dist.CLIENT)
public class CuboidRenderer {
    private static final Vector3f NORMAL = new Vector3f(1, 1, 1).normalize();
    private static final int X_AXIS_MASK = 1 << Direction.Axis.X.ordinal();
    private static final int Y_AXIS_MASK = 1 << Direction.Axis.Y.ordinal();
    private static final int Z_AXIS_MASK = 1 << Direction.Axis.Z.ordinal();

    private CuboidRenderer() {}

    //    public static void renderCuboid(Cuboid cuboid, PoseStack poseStack, VertexConsumer buffer,
    // int frameARGB, FaceDisplay faceDisplay, Camera camera,
    //                                    @Nullable Vec3 renderPos) {
    //        Vec3 pos = camera.getPosition();
    //
    //        poseStack.pushPose();
    //        poseStack.translate(-pos.x, -pos.y, -pos.z);
    //
    //        RenderSystem.disableDepthTest();
    //        RenderSystem.enableBlend();
    //        RenderSystem.disableCull();
    //        RenderSystem.blendFunc(
    //                GlStateManager.SourceFactor.SRC_ALPHA,
    // GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    //        Tesselator tesselator = Tesselator.getInstance();
    //        BufferBuilder buffer = tesselator.getBuilder();
    //
    //        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    //        RenderSystem.setShader(GameRenderer::getPositionColorShader);
    //
    //        RenderBufferUtils.renderCubeFace(
    //                poseStack,
    //                buffer,
    //                cuboid.minX,
    //                cuboid.minY,
    //                cuboid.minZ,
    //                cuboid.maxX,
    //                cuboid.maxY,
    //                cuboid.maxZ,
    //                red(faceARGB),
    //                green(faceARGB),
    //                blue(faceARGB),
    //                alpha(faceARGB),
    //                true);
    //
    //        tesselator.end();
    //
    //        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
    //        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
    //        RenderSystem.lineWidth(3);
    //
    //        RenderBufferUtils.renderCubeFrame(
    //                poseStack,
    //                buffer,
    //                cuboid.minX,
    //                cuboid.minY,
    //                cuboid.minZ,
    //                cuboid.maxX,
    //                cuboid.maxY,
    //                cuboid.maxZ,
    //                red(frameARGB),
    //                green(frameARGB),
    //                blue(frameARGB),
    //                alpha(frameARGB));
    //
    //        tesselator.end();
    //
    //        RenderSystem.enableCull();
    //
    //        RenderSystem.disableBlend();
    //        RenderSystem.enableDepthTest();
    //        poseStack.popPose();
    //    }

    /**
     * @implNote Based off of Tinker's
     */
    public static void renderCuboid(
            Cuboid cuboid,
            PoseStack poseStack,
            VertexConsumer buffer,
            int light,
            int overlay,
            FaceDisplay faceDisplay,
            Camera camera,
            @Nullable Vec3 renderPos) {
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[6];
        int axisToRender = 0;
        // TODO: Eventually try not rendering faces that are covered by things? At the very least for
        // things like multiblocks
        // when one face is entirely casing and not glass
        if (renderPos != null && faceDisplay != FaceDisplay.BOTH) {
            // If we know the position this model is based around in the world, and we aren't displaying
            // both faces
            // then calculate to see if we can skip rendering any faces due to the camera not facing them
            Vec3 camPos = camera.getPosition();
            Vec3 minPos = renderPos.add(cuboid.minX, cuboid.minY, cuboid.minZ);
            Vec3 maxPos = renderPos.add(cuboid.maxX, cuboid.maxY, cuboid.maxZ);
            for (Direction direction : DIRECTIONS) {
                TextureAtlasSprite sprite = cuboid.getSprite(direction);
                if (sprite != null) {
                    Direction.Axis axis = direction.getAxis();
                    Direction.AxisDirection axisDirection = direction.getAxisDirection();
                    double planeLocation =
                            switch (axisDirection) {
                                case POSITIVE -> axis.choose(maxPos.x, maxPos.y, maxPos.z);
                                case NEGATIVE -> axis.choose(minPos.x, minPos.y, minPos.z);
                            };
                    double cameraPosition = axis.choose(camPos.x, camPos.y, camPos.z);
                    // Check whether the camera's position is past the side that it can render on for the face
                    // that we want to be rendering
                    if (faceDisplay.front == (axisDirection == Direction.AxisDirection.NEGATIVE)) {
                        if (cameraPosition >= planeLocation) {
                            sprites[direction.ordinal()] = sprite;
                            axisToRender |= 1 << axis.ordinal();
                        }
                    } else if (cameraPosition <= planeLocation) {
                        sprites[direction.ordinal()] = sprite;
                        axisToRender |= 1 << axis.ordinal();
                    }
                }
            }
        } else {
            for (Direction direction : DIRECTIONS) {
                TextureAtlasSprite sprite = cuboid.getSprite(direction);
                if (sprite != null) {
                    sprites[direction.ordinal()] = sprite;
                    axisToRender |= 1 << direction.getAxis().ordinal();
                }
            }
        }
        if (axisToRender == 0) {
            // Skip rendering if no sides are meant to be rendered
            return;
        }
        // TODO: Further attempt to fix z-fighting at larger distances if we make it not render the
        // sides when it is in a solid block
        // that may improve performance some, but definitely would reduce/remove the majority of
        // remaining z-fighting that is going on
        // Shift it so that the min values are all greater than or equal to zero as the various drawing
        // code
        // has some issues when it comes to handling negative numbers
        int xShift = Mth.floor(cuboid.minX);
        int yShift = Mth.floor(cuboid.minY);
        int zShift = Mth.floor(cuboid.minZ);
        float minX = cuboid.minX - xShift;
        float minY = cuboid.minY - yShift;
        float minZ = cuboid.minZ - zShift;
        float maxX = cuboid.maxX - xShift;
        float maxY = cuboid.maxY - yShift;
        float maxZ = cuboid.maxZ - zShift;
        int xDelta = calculateDelta(minX, maxX);
        int yDelta = calculateDelta(minY, maxY);
        int zDelta = calculateDelta(minZ, maxZ);
        float[] xBounds = getBlockBounds(xDelta, minX, maxX);
        float[] yBounds = getBlockBounds(yDelta, minY, maxY);
        float[] zBounds = getBlockBounds(zDelta, minZ, maxZ);

        poseStack.pushPose();
        poseStack.translate(xShift, yShift, zShift);
        PoseStack.Pose lastMatrix = poseStack.last();
        Matrix4f matrix4f = lastMatrix.pose();
        NormalData normal = new NormalData(lastMatrix.normal(), NORMAL, faceDisplay);

        if ((axisToRender & X_AXIS_MASK) != 0) {
            renderSideXAxis(
                    buffer,
                    cuboid.colors,
                    light,
                    overlay,
                    faceDisplay,
                    xDelta,
                    yDelta,
                    zDelta,
                    sprites,
                    yBounds,
                    zBounds,
                    xBounds,
                    matrix4f,
                    normal);
        }
        if ((axisToRender & Y_AXIS_MASK) != 0) {
            renderSideYAxis(
                    buffer,
                    cuboid.colors,
                    light,
                    overlay,
                    faceDisplay,
                    xDelta,
                    yDelta,
                    zDelta,
                    sprites,
                    yBounds,
                    zBounds,
                    xBounds,
                    matrix4f,
                    normal);
        }
        if ((axisToRender & Z_AXIS_MASK) != 0) {
            renderSideZAxis(
                    buffer,
                    cuboid.colors,
                    light,
                    overlay,
                    faceDisplay,
                    xDelta,
                    yDelta,
                    zDelta,
                    sprites,
                    yBounds,
                    zBounds,
                    xBounds,
                    matrix4f,
                    normal);
        }

        poseStack.popPose();
    }

    private static void renderSideZAxis(
            VertexConsumer buffer,
            int[] colors,
            int light,
            int overlay,
            FaceDisplay faceDisplay,
            int xDelta,
            int yDelta,
            int zDelta,
            TextureAtlasSprite[] sprites,
            float[] yBounds,
            float[] zBounds,
            float[] xBounds,
            Matrix4f matrix4f,
            NormalData normal) {

        TextureAtlasSprite northSprite = sprites[NORTH.ordinal()];
        TextureAtlasSprite southSprite = sprites[SOUTH.ordinal()];
        boolean hasNorth = northSprite != null;
        boolean hasSouth = southSprite != null;

        if (!hasNorth && !hasSouth) {
            return; // sanity check failed
        }

        int colorNorth = colors[NORTH.ordinal()];
        int colorSouth = colors[SOUTH.ordinal()];

        int redNorth = FastColor.ARGB32.red(colorNorth);
        int greenNorth = FastColor.ARGB32.green(colorNorth);
        int blueNorth = FastColor.ARGB32.blue(colorNorth);
        int alphaNorth = FastColor.ARGB32.alpha(colorNorth);
        int redSouth = FastColor.ARGB32.red(colorSouth);
        int greenSouth = FastColor.ARGB32.green(colorSouth);
        int blueSouth = FastColor.ARGB32.blue(colorSouth);
        int alphaSouth = FastColor.ARGB32.alpha(colorSouth);

        // render each side
        for (int y = 0; y <= yDelta; y += 1) {
            float y1 = yBounds[y];
            float y2 = yBounds[y + 1];
            float vBoundsMin = minBound(y1, y2);
            float vBoundsMax = maxBound(y1, y2);

            // Flip V - north
            float minVNorth;
            float maxVNorth;
            if (hasNorth) {
                minVNorth = northSprite.getV(1 - vBoundsMax);
                maxVNorth = northSprite.getV(1 - vBoundsMin);
            } else {
                minVNorth = 0F;
                maxVNorth = 0F;
            }

            // Flip V
            float minVSouth;
            float maxVSouth;
            if (hasSouth) {
                minVSouth = southSprite.getV(1 - vBoundsMax);
                maxVSouth = southSprite.getV(1 - vBoundsMin);
            } else {
                minVSouth = 0F;
                maxVSouth = 0F;
            }

            for (int x = 0; x <= xDelta; x += 1) {
                // start with texture coordinates
                float x1 = xBounds[x];
                float x2 = xBounds[x + 1];

                // choose UV based on opposite two axis
                float uBoundsMin = minBound(x2, x1);
                float uBoundsMax = maxBound(x2, x1);

                float minUNorth;
                float maxUNorth;
                if (hasNorth) {
                    minUNorth = northSprite.getU(uBoundsMin);
                    maxUNorth = northSprite.getU(uBoundsMax);
                } else {
                    minUNorth = 0F;
                    maxUNorth = 0F;
                }

                float minUSouth;
                float maxUSouth;
                if (hasSouth) {
                    minUSouth = southSprite.getU(uBoundsMin);
                    maxUSouth = southSprite.getU(uBoundsMax);
                } else {
                    minUSouth = 0F;
                    maxUSouth = 0F;
                }

                if (hasNorth) {
                    float z1 = zBounds[0];
                    // add quads

                    drawFace(
                            buffer,
                            matrix4f,
                            minUNorth,
                            maxUNorth,
                            minVNorth,
                            maxVNorth,
                            light,
                            overlay,
                            faceDisplay,
                            normal,
                            x1,
                            y1,
                            z1,
                            x1,
                            y2,
                            z1,
                            x2,
                            y2,
                            z1,
                            x2,
                            y1,
                            z1,
                            redNorth,
                            greenNorth,
                            blueNorth,
                            alphaNorth);
                }
                if (hasSouth) {
                    float z2 = zBounds[zDelta + 1];
                    // add quads
                    drawFace(
                            buffer,
                            matrix4f,
                            minUSouth,
                            maxUSouth,
                            minVSouth,
                            maxVSouth,
                            light,
                            overlay,
                            faceDisplay,
                            normal,
                            x2,
                            y1,
                            z2,
                            x2,
                            y2,
                            z2,
                            x1,
                            y2,
                            z2,
                            x1,
                            y1,
                            z2,
                            redSouth,
                            greenSouth,
                            blueSouth,
                            alphaSouth);
                }
            }
        }
    }

    private static void renderSideXAxis(
            VertexConsumer buffer,
            int[] colors,
            int light,
            int overlay,
            FaceDisplay faceDisplay,
            int xDelta,
            int yDelta,
            int zDelta,
            TextureAtlasSprite[] sprites,
            float[] yBounds,
            float[] zBounds,
            float[] xBounds,
            Matrix4f matrix4f,
            NormalData normal) {
        TextureAtlasSprite westSprite = sprites[WEST.ordinal()];
        TextureAtlasSprite eastSprite = sprites[EAST.ordinal()];
        boolean hasWest = westSprite != null;
        boolean hasEast = eastSprite != null;

        if (!hasWest && !hasEast) {
            return; // sanity check failed
        }

        int westColor = colors[WEST.ordinal()];
        int eastColor = colors[EAST.ordinal()];
        int redWest = FastColor.ARGB32.red(westColor);
        int greenWest = FastColor.ARGB32.green(westColor);
        int blueWest = FastColor.ARGB32.blue(westColor);
        int alphaWest = FastColor.ARGB32.alpha(westColor);
        int redEast = FastColor.ARGB32.red(eastColor);
        int greenEast = FastColor.ARGB32.green(eastColor);
        int blueEast = FastColor.ARGB32.blue(eastColor);
        int alphaEast = FastColor.ARGB32.alpha(eastColor);

        // render each side
        for (int y = 0; y <= yDelta; y += 1) {
            float y1 = yBounds[y], y2 = yBounds[y + 1];
            float vBoundsMin = minBound(y1, y2);
            float vBoundsMax = maxBound(y1, y2);

            // Flip V - West
            float minVWest;
            float maxVWest;
            if (hasWest) {
                minVWest = westSprite.getV(1 - vBoundsMax);
                maxVWest = westSprite.getV(1 - vBoundsMin);
            } else {
                minVWest = 0F;
                maxVWest = 0F;
            }

            // Flip V - East
            float minVEast;
            float maxVEast;
            if (hasEast) {
                minVEast = eastSprite.getV(1 - vBoundsMax);
                maxVEast = eastSprite.getV(1 - vBoundsMin);
            } else {
                minVEast = 0F;
                maxVEast = 0F;
            }

            for (int z = 0; z <= zDelta; z += 1) {
                float z1 = zBounds[z];
                float z2 = zBounds[z + 1];
                float uBoundsMin = minBound(z2, z1);
                float uBoundsMax = maxBound(z2, z1);

                float minUWest;
                float maxUWest;
                if (hasWest) {
                    minUWest = westSprite.getU(uBoundsMin);
                    maxUWest = westSprite.getU(uBoundsMax);
                } else {
                    minUWest = 0F;
                    maxUWest = 0F;
                }

                float minUEast;
                float maxUEast;
                if (hasEast) {
                    minUEast = eastSprite.getU(uBoundsMin);
                    maxUEast = eastSprite.getU(uBoundsMax);
                } else {
                    minUEast = 0F;
                    maxUEast = 0F;
                }

                if (hasWest) {
                    float x1 = xBounds[0];
                    // add quads
                    drawFace(
                            buffer,
                            matrix4f,
                            minUWest,
                            maxUWest,
                            minVWest,
                            maxVWest,
                            light,
                            overlay,
                            faceDisplay,
                            normal,
                            x1,
                            y1,
                            z2,
                            x1,
                            y2,
                            z2,
                            x1,
                            y2,
                            z1,
                            x1,
                            y1,
                            z1,
                            redWest,
                            greenWest,
                            blueWest,
                            alphaWest);
                }
                if (hasEast) {
                    float x2 = xBounds[xDelta + 1];
                    // add quads
                    drawFace(
                            buffer,
                            matrix4f,
                            minUEast,
                            maxUEast,
                            minVEast,
                            maxVEast,
                            light,
                            overlay,
                            faceDisplay,
                            normal,
                            x2,
                            y1,
                            z1,
                            x2,
                            y2,
                            z1,
                            x2,
                            y2,
                            z2,
                            x2,
                            y1,
                            z2,
                            redEast,
                            greenEast,
                            blueEast,
                            alphaEast);
                }
            }
        }
    }

    private static void renderSideYAxis(
            VertexConsumer buffer,
            int[] colors,
            int light,
            int overlay,
            FaceDisplay faceDisplay,
            int xDelta,
            int yDelta,
            int zDelta,
            TextureAtlasSprite[] sprites,
            float[] yBounds,
            float[] zBounds,
            float[] xBounds,
            Matrix4f matrix4f,
            NormalData normal) {
        TextureAtlasSprite upSprite = sprites[UP.ordinal()];
        TextureAtlasSprite downSprite = sprites[DOWN.ordinal()];
        boolean hasUp = upSprite != null;
        boolean hasDown = downSprite != null;

        if (!hasUp && !hasDown) {
            return; // sanity check failed
        }

        int downColor = colors[DOWN.ordinal()];
        int upColor = colors[UP.ordinal()];
        int redUp = FastColor.ARGB32.red(upColor);
        int greenUp = FastColor.ARGB32.green(upColor);
        int blueUp = FastColor.ARGB32.blue(upColor);
        int alphaUp = FastColor.ARGB32.alpha(upColor);
        int redDown = FastColor.ARGB32.red(downColor);
        int greenDown = FastColor.ARGB32.green(downColor);
        int blueDown = FastColor.ARGB32.blue(downColor);
        int alphaDown = FastColor.ARGB32.alpha(downColor);

        // render each side
        for (int z = 0; z <= zDelta; z += 1) {
            float z1 = zBounds[z];
            float z2 = zBounds[z + 1];
            float vBoundsMin = minBound(z2, z1);
            float vBoundsMax = maxBound(z2, z1);
            // Flip V - Up
            float minVUp;
            float maxVUp;
            if (hasUp) {
                minVUp = upSprite.getV(1 - vBoundsMax);
                maxVUp = upSprite.getV(1 - vBoundsMin);
            } else {
                minVUp = 0F;
                maxVUp = 0F;
            }
            // Flip V - Down
            float minV;
            float maxV;
            if (hasDown) {
                minV = downSprite.getV(1 - vBoundsMax);
                maxV = downSprite.getV(1 - vBoundsMin);
            } else {
                minV = 0F;
                maxV = 0F;
            }

            for (int x = 0; x <= xDelta; x += 1) {
                float x1 = xBounds[x];
                float x2 = xBounds[x + 1];

                float uBoundsMin = minBound(x1, x2);
                float uBoundsMax = maxBound(x1, x2);

                float minUUp;
                float maxUUp;
                if (hasUp) {
                    minUUp = upSprite.getU(uBoundsMin);
                    maxUUp = upSprite.getU(uBoundsMax);
                } else {
                    minUUp = 0F;
                    maxUUp = 0F;
                }

                float minU;
                float maxU;
                if (hasDown) {
                    minU = downSprite.getU(uBoundsMin);
                    maxU = downSprite.getU(uBoundsMax);
                } else {
                    minU = 0F;
                    maxU = 0F;
                }

                if (hasUp) {
                    float y2 = yBounds[yDelta + 1];
                    // add quads
                    drawFace(
                            buffer,
                            matrix4f,
                            minUUp,
                            maxUUp,
                            minVUp,
                            maxVUp,
                            light,
                            overlay,
                            faceDisplay,
                            normal,
                            x1,
                            y2,
                            z1,
                            x1,
                            y2,
                            z2,
                            x2,
                            y2,
                            z2,
                            x2,
                            y2,
                            z1,
                            redUp,
                            greenUp,
                            blueUp,
                            alphaUp);
                }
                if (hasDown) {
                    float y1 = yBounds[0];
                    // add quads
                    drawFace(
                            buffer,
                            matrix4f,
                            minU,
                            maxU,
                            minV,
                            maxV,
                            light,
                            overlay,
                            faceDisplay,
                            normal,
                            x1,
                            y1,
                            z2,
                            x1,
                            y1,
                            z1,
                            x2,
                            y1,
                            z1,
                            x2,
                            y1,
                            z2,
                            redDown,
                            greenDown,
                            blueDown,
                            alphaDown);
                }
            }
        }
    }

    /**
     * @implNote From Tinker's
     */
    private static float[] getBlockBounds(int delta, float start, float end) {
        float[] bounds = new float[2 + delta];
        bounds[0] = start;
        int offset = (int) start;
        for (int i = 1; i <= delta; i++) {
            bounds[i] = i + offset;
        }
        bounds[delta + 1] = end;
        return bounds;
    }

    /**
     * @implNote From Tinker's
     */
    private static int calculateDelta(float min, float max) {
        // The texture can stretch over more blocks than the subtracted height is if min's decimal is
        // bigger than max's decimal (causing UV over 1)
        // ignoring the decimals prevents this, as yd then equals exactly how many ints are between the
        // two
        // for example, if max = 5.1 and min = 2.3, 2.8 (which rounds to 2), with the face array
        // becoming 2.3, 3, 4, 5.1
        int delta = (int) (max - (int) min);
        // except in the rare case of max perfectly aligned with the block, causing the top face to
        // render multiple times
        // for example, if max = 3 and min = 1, the values of the face array become 1, 2, 3, 3 as we
        // then have middle ints
        if (max % 1d == 0) {
            delta--;
        }
        return delta;
    }

    private static void drawFace(
            VertexConsumer buffer,
            Matrix4f matrix,
            float minU,
            float maxU,
            float minV,
            float maxV,
            int light,
            int overlay,
            FaceDisplay faceDisplay,
            NormalData normal,
            float x1,
            float y1,
            float z1,
            float x2,
            float y2,
            float z2,
            float x3,
            float y3,
            float z3,
            float x4,
            float y4,
            float z4,
            int red,
            int green,
            int blue,
            int alpha) {
        if (faceDisplay.front) {
            buffer
                    .vertex(matrix, x1, y1, z1)
                    .color(red, green, blue, alpha)
                    .uv(minU, maxV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.front.x(), normal.front.y(), normal.front.z());
            buffer
                    .vertex(matrix, x2, y2, z2)
                    .color(red, green, blue, alpha)
                    .uv(minU, minV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.front.x(), normal.front.y(), normal.front.z());
            buffer
                    .vertex(matrix, x3, y3, z3)
                    .color(red, green, blue, alpha)
                    .uv(maxU, minV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.front.x(), normal.front.y(), normal.front.z());
            buffer
                    .vertex(matrix, x4, y4, z4)
                    .color(red, green, blue, alpha)
                    .uv(maxU, maxV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.front.x(), normal.front.y(), normal.front.z());
        }
        if (faceDisplay.back) {
            buffer
                    .vertex(matrix, x4, y4, z4)
                    .color(red, green, blue, alpha)
                    .uv(maxU, maxV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.back.x(), normal.back.y(), normal.back.z());
            buffer
                    .vertex(matrix, x3, y3, z3)
                    .color(red, green, blue, alpha)
                    .uv(maxU, minV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.back.x(), normal.back.y(), normal.back.z());
            buffer
                    .vertex(matrix, x2, y2, z2)
                    .color(red, green, blue, alpha)
                    .uv(minU, minV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.back.x(), normal.back.y(), normal.back.z());
            buffer
                    .vertex(matrix, x1, y1, z1)
                    .color(red, green, blue, alpha)
                    .uv(minU, maxV)
                    .overlayCoords(overlay)
                    .uv2(light)
                    .normal(normal.back.x(), normal.back.y(), normal.back.z());
        }
    }

    private static float minBound(float min, float max) {
        // wrap UV to be between 0 and 1, assumes none of the positions lie outside the 0, 0, 0 to 1, 1,
        // 1 range
        // however, one of them might be exactly on the 1.0 bound, that one should be set to 1 instead
        // of left at 0
        boolean bigger = min > max;
        min = min % 1;
        if (bigger) {
            return min == 0 ? 1 : min;
        }
        return min;
    }

    private static float maxBound(float min, float max) {
        // wrap UV to be between 0 and 1, assumes none of the positions lie outside the 0, 0, 0 to 1, 1,
        // 1 range
        // however, one of them might be exactly on the 1.0 bound, that one should be set to 1 instead
        // of left at 0
        boolean bigger = min > max;
        max = max % 1;
        if (bigger) {
            return max;
        }
        return max == 0 ? 1 : max;
    }

    /**
     * Used to only have to calculate normals once rather than transforming based on the matrix for every vertex call. If a face shouldn't be displayed the normal vector
     * will be zero.
     */
    private record NormalData(Vector3f front, Vector3f back) {

        private NormalData(Matrix3f normalMatrix, Vector3f normal, FaceDisplay faceDisplay) {
            this(
                    faceDisplay.front
                            ? calculate(normalMatrix, normal.x(), normal.y(), normal.z())
                            : new Vector3f(),
                    faceDisplay.back
                            ? calculate(normalMatrix, -normal.x(), -normal.y(), -normal.z())
                            : new Vector3f());
        }

        private static Vector3f calculate(Matrix3f normalMatrix, float x, float y, float z) {
            Vector3f matrixAdjustedNormal = new Vector3f(x, y, z);
            return matrixAdjustedNormal.mul(normalMatrix);
        }
    }

    public enum FaceDisplay {
        FRONT(true, false),
        BACK(false, true),
        BOTH(true, true);

        private final boolean front;
        private final boolean back;

        FaceDisplay(boolean front, boolean back) {
            this.front = front;
            this.back = back;
        }
    }
}
