package com.epimorphismmc.monomorphism.client.model.pipeline;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@OnlyIn(Dist.CLIENT)
public class Vertex {

    private final VertexFormat format;
    private float x, y, z;
    private float u, v;
    private float r, g, b, a;
    private float nX, nY, nZ;

    public Vertex(VertexFormat format) {
        this.format = format;
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setUV(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public void setRGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setNormal(float x, float y, float z) {
        this.nX = x;
        this.nY = y;
        this.nZ = z;
    }

    public void applyVertexData(VertexConsumer consumer) {
        for (int index = 0; index < this.format.getElements().size(); index++) {
            applyVertexDataForType(index, this.format.getElements().get(index), consumer);
        }
    }

    // TODO
    private void applyVertexDataForType(
            int index, VertexFormatElement element, VertexConsumer consumer) {
        switch (element.getUsage()) {
            case POSITION:
                consumer.vertex(x, y, z);
                break;
            case UV:
                // UV exists for two different VertexFormatElements; one is texture, another light map
                if (element.getType() == VertexFormatElement.Type.FLOAT) {
                    // We are certain this is texture, put the UV's
                    consumer.uv(u, v);
                } else {
                    // This is for light map, put (0, 0) for automatic light map
                    consumer.uv2(0, 0);
                }
                break;
            case COLOR:
                consumer.color(r, g, b, a);
                break;
            case NORMAL:
                consumer.normal(nX, nY, nZ);
                break;
            default:
                // We don't care about PADDING or other elements

        }
    }
}
