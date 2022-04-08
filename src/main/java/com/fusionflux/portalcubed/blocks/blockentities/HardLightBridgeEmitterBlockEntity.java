package com.fusionflux.portalcubed.blocks.blockentities;

import com.fusionflux.portalcubed.blocks.PortalCubedBlocks;
import com.fusionflux.portalcubed.config.PortalCubedConfig;
import com.fusionflux.portalcubed.entity.BridgeEntity;
import com.fusionflux.portalcubed.entity.PortalCubedEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * @author sailKite
 * @author FusionFlux
 * <p>
 * Handles the operating logic for the {@link HardLightBridgeEmitterBlock} and their associated bridges.
 */
public class HardLightBridgeEmitterBlockEntity extends BlockEntity {

    public final int MAX_RANGE = PortalCubedConfig.get().numbersblock.maxBridgeLength;
    public List<BlockPos.Mutable> bridges;
    public UUID bridge;

    public HardLightBridgeEmitterBlockEntity(BlockPos pos, BlockState state) {
        super(PortalCubedBlocks.HLB_EMITTER_ENTITY,pos,state);
        this.bridges = new ArrayList<>();
        this.bridge = Util.NIL_UUID;
    }
    public static void tick(World world, BlockPos pos, BlockState state, HardLightBridgeEmitterBlockEntity blockEntity) {
        Direction facing = state.get(Properties.FACING);
        Box blockBox = new Box(pos,new BlockPos(pos.getX()+1,pos.getY()+1,pos.getZ()+1));
        Vec3d startPos = new Vec3d(blockBox.getCenter().x-(.5 * facing.getVector().getX()),blockBox.getCenter().y-.5,blockBox.getCenter().z-(.5 * facing.getVector().getZ()));
        Vec3d invertDirection = Vec3d.ZERO;
        if(facing.getVector().getX() != 0){
            invertDirection = new Vec3d(0,1,1);
        }
        if(facing.getVector().getZ() != 0){
            invertDirection = new Vec3d(1,1,0);
        }

        startPos = startPos.add(new Vec3d(invertDirection.x*0.375,invertDirection.y*0.8125,invertDirection.z*0.375));
        System.out.println(startPos);
        if (!world.isClient) {
            boolean redstonePowered = world.isReceivingRedstonePower(blockEntity.getPos());
            Vec3d entityPos = new Vec3d(pos.getX(),pos.getY(),pos.getZ());
            if (redstonePowered) {

                if (!world.getBlockState(pos).get(Properties.POWERED)) {
                    blockEntity.togglePowered(world.getBlockState(pos));
                }


                BlockPos.Mutable translatedPos = pos.mutableCopy();

                //if (blockEntity.bridges != null) {

                int bridgeLength = 0;
                    for (int i = 0; i <= blockEntity.MAX_RANGE; i++) {
                        translatedPos.move(facing);
                        if (world.isAir(translatedPos) || world.getBlockState(translatedPos).getHardness(world, translatedPos) <= 0.1F) {
                            bridgeLength++;
                        } else {
                            Box blockPosEnd = new Box(translatedPos,new BlockPos(translatedPos.getX()+1,translatedPos.getY()+1,translatedPos.getZ()+1));
                            Vec3d endPos = new Vec3d(blockPosEnd.getCenter().x-(.5 * facing.getVector().getX()),blockPosEnd.getCenter().y-.5,blockPosEnd.getCenter().z-(.5 * facing.getVector().getZ()));
                            if(facing.getVector().getX() != 0){
                                invertDirection = new Vec3d(0,1,1);
                            }
                            if(facing.getVector().getZ() != 0){
                                invertDirection = new Vec3d(1,1,0);
                            }

                            endPos = endPos.add(new Vec3d(invertDirection.x*-0.375,invertDirection.y*0.75,invertDirection.z*-0.375));
                            //blockEntity.bridges = modfunnels;
                            if (bridgeLength != 0) {
                                Vec3d translation = new Vec3d(facing.getVector().getX()*(bridgeLength/2d),facing.getVector().getY(),facing.getVector().getZ()*(bridgeLength/2d));
                                //entityPos=entityPos.add(translation);
                                if(blockEntity.bridge == null) {


                                    BridgeEntity bridge = PortalCubedEntities.BRIDGE.create(world);
                                    assert bridge != null;
                                    Box constructedBox = new Box(startPos, endPos);
                                    bridge.setDirection(new BlockPos(facing.getVector()));
                                    bridge.setBoundingBoxPos(pos,translatedPos);
                                    bridge.setBoundingBox(constructedBox);
                                    bridge.setPos(entityPos.x, entityPos.y, entityPos.z);
                                    world.spawnEntity(bridge);
                                    blockEntity.bridge = bridge.getUuid();
                                }else{
                                    BridgeEntity bridgeGot = (BridgeEntity) ((ServerWorld) world).getEntity(blockEntity.bridge);
                                    if(bridgeGot == null){
                                        BridgeEntity bridge = PortalCubedEntities.BRIDGE.create(world);
                                        assert bridge != null;
                                        Box constructedBox = new Box(startPos, endPos);
                                        bridge.setBoundingBox(constructedBox);
                                        bridge.setDirection(new BlockPos(facing.getVector()));
                                        bridge.setBoundingBoxPos(pos,translatedPos);
                                        bridge.setPos(entityPos.x, entityPos.y, entityPos.z);
                                        world.spawnEntity(bridge);
                                        blockEntity.bridge = bridge.getUuid();
                                    }else{
                                        bridgeGot.setDirection(new BlockPos(facing.getVector()));
                                        bridgeGot.setBoundingBoxPos(pos,translatedPos);
                                        Box constructedBox = new Box(startPos, endPos);
                                        bridgeGot.setBoundingBox(constructedBox);
                                       // bridgeGot.setPos(entityPos.x,entityPos.y,entityPos.z);
                                        //bridgeGot.setPosition(entityPos);
                                    }
                                }
                             //   bridge.createBoundingBox(pos,translatedPos.add(1,1,1));
                               // }
                            }
                            break;
                        }
                    }
                }

            if (!redstonePowered) {
                if (world.getBlockState(pos).get(Properties.POWERED)) {
                    blockEntity.togglePowered(world.getBlockState(pos));
                }
            }
            }

        }


   // }

    public void playSound(SoundEvent soundEvent) {
        this.world.playSound(null, this.pos, soundEvent, SoundCategory.BLOCKS, 0.1F, 3.0F);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        List<Integer> posXList = new ArrayList<>();
        List<Integer> posYList = new ArrayList<>();
        List<Integer> posZList = new ArrayList<>();

        tag.putUuid("bridge",bridge);

        for(BlockPos pos : bridges){
            posXList.add(pos.getX());
            posYList.add(pos.getY());
            posZList.add(pos.getZ());
        }

        tag.putIntArray("xList", posXList);
        tag.putIntArray("yList", posYList);
        tag.putIntArray("zList", posZList);

        tag.putInt("size", bridges.size());
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        List<Integer> posXList = new ArrayList<>();
        List<Integer> posYList = new ArrayList<>();
        List<Integer> posZList = new ArrayList<>();

        posXList = Arrays.asList(ArrayUtils.toObject(tag.getIntArray("xList")));
        posYList = Arrays.asList(ArrayUtils.toObject(tag.getIntArray("yList")));
        posZList = Arrays.asList(ArrayUtils.toObject(tag.getIntArray("zList")));

        bridge = (tag.getUuid("bridge"));

        int size = tag.getInt("size");

        if(!bridges.isEmpty())
            bridges.clear();

        for (int i = 0; i < size; i++) {
            bridges.add(new BlockPos.Mutable(posXList.get(i), posYList.get(i), posZList.get(i)));
        }

       // bridge = tag.getUuid("bridge");



    }

    public void togglePowered(BlockState state) {
        assert world != null;
        world.setBlockState(pos, state.cycle(Properties.POWERED));
        if (world.getBlockState(pos).get(Properties.POWERED)) {
            this.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
        }
        if (!world.getBlockState(pos).get(Properties.POWERED)) {
            this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
        }
    }

}