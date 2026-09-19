package app.morphe.patches.anilili

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.Constants.COMPATIBILITY_ANILILI
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val fixRecycledBitmapCrashPatch = bytecodePatch(
    name = "Fix Recycled Bitmap Crash",
    description = "Fixes recycled bitmap crashes on Android 14/15+ during media playback metadata updates and notification creation.",
    default = true
) {
    compatibleWith(COMPATIBILITY_ANILILI)

    extendWith("extensions/extension.mpe")

    execute {
        classDefForEach { classDef ->
            val mutableClass by lazy { mutableClassDefBy(classDef) }

            classDef.methods.forEachIndexed { methodIndex, method ->
                val implementation = method.implementation ?: return@forEachIndexed
                val instructions = implementation.instructions.toList()

                instructions.forEachIndexed { instrIndex, instruction ->
                    if (instruction is ReferenceInstruction && instruction.reference is MethodReference) {
                        val ref = instruction.reference as MethodReference
                        val definingClass = ref.definingClass
                        val methodName = ref.name

                        // 1. MediaMetadata.Builder.putBitmap
                        if (definingClass == "Landroid/media/MediaMetadata${'$'}Builder;" && methodName == "putBitmap") {
                            if (instruction is Instruction35c) {
                                val vBld = "v${instruction.registerC}"
                                val vKey = "v${instruction.registerD}"
                                val vBm = "v${instruction.registerE}"
                                mutableClass.methods[methodIndex].replaceInstruction(
                                    instrIndex,
                                    "invoke-static {$vBld, $vKey, $vBm}, Lcom/thegibbonn/extension/BitmapCrashGuard;->safePutBitmap(Landroid/media/MediaMetadata${'$'}Builder;Ljava/lang/String;Landroid/graphics/Bitmap;)Landroid/media/MediaMetadata${'$'}Builder;"
                                )
                            }
                        }

                        // 2. Icon.createWithBitmap
                        else if (definingClass == "Landroid/graphics/drawable/Icon;" && methodName == "createWithBitmap") {
                            val reg = when (instruction) {
                                is Instruction21c -> "v${instruction.registerA}"
                                is Instruction35c -> "v${instruction.registerC}"
                                else -> null
                            }
                            if (reg != null) {
                                mutableClass.methods[methodIndex].replaceInstruction(
                                    instrIndex,
                                    "invoke-static {$reg}, Lcom/thegibbonn/extension/BitmapCrashGuard;->safeCreateWithBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;"
                                )
                            }
                        }

                        // 3. Icon.createWithAdaptiveBitmap
                        else if (definingClass == "Landroid/graphics/drawable/Icon;" && methodName == "createWithAdaptiveBitmap") {
                            val reg = when (instruction) {
                                is Instruction21c -> "v${instruction.registerA}"
                                is Instruction35c -> "v${instruction.registerC}"
                                else -> null
                            }
                            if (reg != null) {
                                mutableClass.methods[methodIndex].replaceInstruction(
                                    instrIndex,
                                    "invoke-static {$reg}, Lcom/thegibbonn/extension/BitmapCrashGuard;->safeCreateWithAdaptiveBitmap(Landroid/graphics/Bitmap;)Landroid/graphics/drawable/Icon;"
                                )
                            }
                        }

                        // 4. IconCompat.toIcon or IconCompat.e
                        else if (definingClass == "Landroidx/core/graphics/drawable/IconCompat;" && (methodName == "toIcon" || methodName == "e")) {
                            if (instruction is Instruction35c) {
                                val vIcon = "v${instruction.registerC}"
                                val vCtx = "v${instruction.registerD}"
                                mutableClass.methods[methodIndex].replaceInstruction(
                                    instrIndex,
                                    "invoke-static {$vIcon, $vCtx}, Lcom/thegibbonn/extension/BitmapCrashGuard;->safeToIcon(Landroidx/core/graphics/drawable/IconCompat;Landroid/content/Context;)Landroid/graphics/drawable/Icon;"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
