package io.github.thewebcode.yplugin.nms.minecraft_1_20_R1;

import io.github.thewebcode.yplugin.effect.ParticleEffect;
import io.github.thewebcode.yplugin.nms.ParticleEffectsHandler;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import org.joor.Reflect;

public class ParticleEffectsHandler_1_20_R1 implements ParticleEffectsHandler {

    private Class<Enum> enumParticleClass;

    private Class<?> particlePacketClass;

    public ParticleEffectsHandler_1_20_R1() {
        this.enumParticleClass = (Class<Enum>) ReflectionUtilities.getNMSClass("EnumParticle");
        this.particlePacketClass = ReflectionUtilities.getNMSClass("PacketPlayOutWorldParticles");
    }

    @Override
    public Object createParticleEffectPacket(ParticleEffect effect, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int count, int... extra) {
        return Reflect.on(particlePacketClass).create()
                .set("a", Reflect.on(this.enumParticleClass).call("valueOf",effect.getName()).get())
                .set("j", true)
                .set("b", x)
                .set("c", y)
                .set("d", z)
                .set("e", offsetX)
                .set("f", offsetY)
                .set("g", offsetZ)
                .set("h", speed)
                .set("i", count)
                .get();
    }
}
