package com.thommil.libgdx.runtime.physics.particles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import finnstr.libgdx.liquidfun.ParticleDef;
import finnstr.libgdx.liquidfun.ParticleGroup;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory used to create custom ParticleGroups
 *
 * Created by thommil on 21/02/16.
 */
public class ParticleGroupFactory {

    public static int GROUP_DEFAULT_WIDTH = 32;
    public static int GROUP_DEFAULT_HEIGHT = 32;
    public static int PARTCILES_COMPONENTS = 4;
    public static int PARTICLES_DATA_REALLOC_SIZE = GROUP_DEFAULT_WIDTH * GROUP_DEFAULT_HEIGHT * PARTCILES_COMPONENTS;

    /**
     * List of instances
     */
    private static Map<ParticleSystem, ParticleGroupFactory> instances = new HashMap<ParticleSystem, ParticleGroupFactory>();

    /**
     * The bound ParticleSystem
     */
    private ParticleSystem particleSystem;

    /**
     * Factory accessor
     *
     * @param particleSystem The ParticleSystem bound to the factory
     *
     * @return The ParticleGroupFactory instance for the corresponding ParticleSystem
     */
    public static ParticleGroupFactory getinstance(final ParticleSystem particleSystem){
        ParticleGroupFactory softBodyFactory = ParticleGroupFactory.instances.get(particleSystem);
        if(softBodyFactory == null){
            softBodyFactory = new ParticleGroupFactory(particleSystem);
            ParticleGroupFactory.instances.put(particleSystem,softBodyFactory);
        }
        return softBodyFactory;
    }

    private ParticleGroupFactory(final ParticleSystem particleSystem){
        this.particleSystem = particleSystem;
    }

    /**
     * Creates a ParticleGroup with specifiec definition.
     * This function should be used instead of the native one to use other ParticleGroupFactory helpers.
     *
     * @param particleGroupDef The ParticleGroup definition
     *
     * @return The newly created ParticleGroup
     */
    public ParticleGroup createGroup(final ParticleGroupDef particleGroupDef){
        ParticleGroup group = this.particleSystem.createParticleGroup(particleGroupDef);
        group.setUsetData(new GroupData());
        GroupData groupData = new GroupData();
        groupData.radius = this.particleSystem.getParticleRadius();
        groupData.stride = particleGroupDef.stride;
        group.setUsetData(groupData);
        return group;
    }

    /**
     * Creates an empty ParticleGroup with specifiec definition
     * This function should be used instead of the native one to use other ParticleGroupFactory helpers.
     *
     * @param particleGroupDef The ParticleGroup definition
     *
     * @return The newly created empty ParticleGroup
     */
    public ParticleGroup createEmptyGroup(final ParticleGroupDef particleGroupDef){
        ParticleGroup group;
        final CircleShape shape = new CircleShape();
        shape.setRadius(0);
        particleGroupDef.shape = shape;
        group = this.particleSystem.createParticleGroup(particleGroupDef);
        GroupData groupData = new GroupData();
        groupData.radius = this.particleSystem.getParticleRadius();
        groupData.stride = particleGroupDef.stride;
        group.setUsetData(groupData);
        return group;
    }

    /**
     * Adds a particle to an existing group based (the particle position becomes relative to group center
     * and the position is set based on stride). This method canbe applied on groups created by ParticleGroupFactory
     * only.
     *
     * @param particleDef The particle definition
     * @param group The ParticleGroup to use
     */
    public void addParticleToGroup(final int u, final int v, final float color, final ParticleDef particleDef, final ParticleGroup group){
        final GroupData groupData  = (GroupData) group.getUserData();

        final Vector2 groupPosition = group.getPosition();
        particleDef.position.set(groupPosition.x + (u * groupData.stride),
                                    groupPosition.y + (v * groupData.stride));
        //particleDef.group = group;
        ParticleGroupDef particleGroupDef =  new ParticleGroupDef();
        particleGroupDef.stride = 0.3f;
        particleGroupDef.flags.add(ParticleDef.ParticleType.b2_barrierParticle);
        ParticleGroup tmpGroup = this.createEmptyGroup(particleGroupDef);
        tmpGroup.setGroupFlags(group.getGroupFlags());
        particleDef.group = tmpGroup;
        int index = this.particleSystem.createParticle(particleDef);
        this.particleSystem.joinParticleGroups(group,tmpGroup);

        this.addParticleData(index, u, v, color, groupData);
    }



    private void addParticleData(final int index, final int u, final int v, final float color, final GroupData groupData){
        final int particleOffset = groupData.count * PARTCILES_COMPONENTS;
        if(groupData.count * PARTCILES_COMPONENTS == particleOffset){
            final float reallocArray[] = new float[groupData.particlesData.length + PARTICLES_DATA_REALLOC_SIZE];
            System.arraycopy(groupData.particlesData, 0, reallocArray, 0, particleOffset);
            groupData.particlesData = reallocArray;
        }
        groupData.particlesData[particleOffset + GroupData.INDEX] = index;
        groupData.particlesData[particleOffset + GroupData.U] = u;
        groupData.particlesData[particleOffset + GroupData.V] = v;
        groupData.particlesData[particleOffset + GroupData.COLOR] = color;

        if(u >= groupData.width) groupData.width = u + 1;
        if(v >= groupData.height) groupData.height = v + 1;

        groupData.count++;
    }

    /**
     * UserData bound to groups created by Factory, custom UserData of these groups can be set
     * using the underlying pointer userData.
     */
    private static class GroupData{

        public static final int INDEX = 0;
        public static final int U = 1;
        public static final int V = 2;
        public static final int COLOR = 3;

        public Object userData = null;
        public float radius = 0;
        public float stride = 0;
        public int width = 0;
        public int height = 0;
        public int count = 0;
        float[] particlesData = new float[PARTICLES_DATA_REALLOC_SIZE];
    }

}
