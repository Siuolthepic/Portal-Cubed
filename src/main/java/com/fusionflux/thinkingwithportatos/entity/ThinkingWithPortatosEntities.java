package com.fusionflux.thinkingwithportatos.entity;

import com.fusionflux.thinkingwithportatos.ThinkingWithPortatos;
import dev.lazurite.rayon.core.api.PhysicsElement;
import dev.lazurite.rayon.core.api.event.ElementCollisionEvents;
import dev.lazurite.rayon.core.impl.physics.space.body.BlockRigidBody;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.concurrent.Executor;

public class ThinkingWithPortatosEntities {
    public static final EntityType<CubeEntity> CUBE = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CubeEntity::new)
            .dimensions(EntityDimensions.fixed(0.625F, 0.625F))
            .build();

    public static final EntityType<CompanionCubeEntity> COMPANION_CUBE = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CompanionCubeEntity::new)
            .dimensions(EntityDimensions.fixed(0.625F, 0.625F))
            .build();




    public static final EntityType<GelOrbEntity> GEL_ORB = FabricEntityTypeBuilder.<GelOrbEntity>create(SpawnGroup.MISC, GelOrbEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
            .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
            .build(); // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
    public static final EntityType<RepulsionGelOrbEntity> REPULSION_GEL_ORB = FabricEntityTypeBuilder.<RepulsionGelOrbEntity>create(SpawnGroup.MISC, RepulsionGelOrbEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
            .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
            .build(); // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS



    public static void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(ThinkingWithPortatos.MODID, "cube"), CUBE);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(ThinkingWithPortatos.MODID, "companion_cube"), COMPANION_CUBE);

        Registry.register(Registry.ENTITY_TYPE, new Identifier(ThinkingWithPortatos.MODID, "gel_orb"), GEL_ORB);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(ThinkingWithPortatos.MODID, "repulsion_gel_orb"), REPULSION_GEL_ORB);
        ElementCollisionEvents.BLOCK_COLLISION.register(ThinkingWithPortatosEntities::doBlockCollisions);
    }

    public static void doBlockCollisions(Executor executor, PhysicsElement element, BlockRigidBody block, float impulse) {
        executor.execute(() -> {
            if (element instanceof CubeEntity) {
                ((CubeEntity) element).onCollision(impulse);
            }
        });
    }
}
