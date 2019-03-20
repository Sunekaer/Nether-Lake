package com.sunekaer.mods.netherlake.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class PortalCT implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraft.world.Teleporter")){
            ClassNode classNode = readClassFromBytes(basicClass);
            for(MethodNode methodNode : classNode.methods){
                if(methodNode.name.equals("makePortal") || methodNode.name.equals("func_85188_a")){
                    AbstractInsnNode[] nodes = methodNode.instructions.toArray();
                    int injectionPoint = -1;
                    for (int i = 0; i < nodes.length; i++) {
                        if(nodes[i] instanceof MethodInsnNode){
                            MethodInsnNode methodInsnNode = (MethodInsnNode) nodes[i];
                            if(methodInsnNode.name.equals("clamp") || methodInsnNode.name.equals("func_76125_a")){
                                injectionPoint = i + 4;
                            }
                        }
                    }
                    if(injectionPoint == -1){
                        throw new RuntimeException("Failed to find instruction point");
                    }

                    InsnList insnList = new InsnList();
                    insnList.insert(new IntInsnNode(Opcodes.BIPUSH, 45));
                    insnList.insert(new VarInsnNode(Opcodes.ISTORE, 9));

                    methodNode.instructions.insert(nodes[injectionPoint], insnList);
                }
            }
            return writeClassToBytes(classNode);
        }
        return basicClass;
    }

    public void test(){
        int i = 12;
        if(true){
            i = 53;
        }
    }

    public static ClassNode readClassFromBytes(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        return classNode;
    }

    public static byte[] writeClassToBytes(ClassNode classNode) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
