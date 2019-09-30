package enemeez.simplefarming.events;

import java.lang.reflect.Method;
import java.util.List;

import enemeez.simplefarming.SimpleFarming;
import enemeez.simplefarming.blocks.CustomBush;
import enemeez.simplefarming.blocks.CustomCactus;
import enemeez.simplefarming.blocks.DoubleCrop;
import enemeez.simplefarming.blocks.FruitLeaves;
import enemeez.simplefarming.blocks.GrapeBlock;
import enemeez.simplefarming.blocks.WildPlant;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ModHarvest {
	private static final Method seedDrops;

	static {
		seedDrops = ObfuscationReflectionHelper.findMethod(CropsBlock.class, "func_199772_f");
	}

	private Item getCropSeed(Block block) {
		try {
			return (Item) seedDrops.invoke(block);
		}

		catch (Exception e) {
			SimpleFarming.LOGGER.error("Error finding seed", e.getLocalizedMessage());
		}

		return null;
	}

	@SubscribeEvent
	public void onCropHarvest(RightClickBlock event) {
		if (event.getPlayer().getHeldItemMainhand().getItem() != Items.BONE_MEAL) {
			List<ItemStack> drops;
			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof CropsBlock
					&& !(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof DoubleCrop)) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true); // prevents blocks from being placed
				CropsBlock crop = (CropsBlock) event.getWorld().getBlockState(event.getPos()).getBlock();
				if (crop.isMaxAge(event.getWorld().getBlockState(event.getPos()))) {
					if (!event.getWorld().isRemote) {
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							if (drops.get(i).getItem() != getCropSeed(crop))
								event.getWorld()
										.addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(),
												(ItemStack) drops.get(i)));
							if (crop == Blocks.POTATOES || crop == Blocks.CARROTS) {
								drops.remove(0);
								event.getWorld()
										.addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(),
												(ItemStack) drops.get(i)));
							}
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(), SoundEvents.BLOCK_CROP_BREAK,
								SoundCategory.BLOCKS, 1.0F, 0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), crop.getDefaultState(), 2);

					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}
			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof CustomBush) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				CustomBush bush = (CustomBush) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (bush.isMaxAge(event.getWorld().getBlockState(event.getPos()))) {
					if (!event.getWorld().isRemote) {
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							if (drops.get(i).getItem() != bush.getItem(event.getWorld(), event.getPos(),
									event.getWorld().getBlockState(event.getPos())).getItem())
								event.getWorld()
										.addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(),
												(ItemStack) drops.get(i)));
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(),
								SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F,
								0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), bush.getDefaultState(), 2);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}
			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof CustomCactus) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				CustomCactus cactus = (CustomCactus) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (cactus.isMaxAge(event.getWorld().getBlockState(event.getPos()))) {
					if (!event.getWorld().isRemote) {
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							if (drops.get(i).getItem() != cactus.getItem(event.getWorld(), event.getPos(),
									event.getWorld().getBlockState(event.getPos())).getItem())
								event.getWorld()
										.addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(),
												(ItemStack) drops.get(i)));
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(),
								SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F,
								0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), cactus.getDefaultState(), 2);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}
			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof FruitLeaves) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				FruitLeaves leaf = (FruitLeaves) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (leaf.isMaxAge(event.getWorld().getBlockState(event.getPos()))) {
					if (!event.getWorld().isRemote) {
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							if (drops.get(i).getItem() != leaf.getSapling(event.getWorld(), event.getPos(),
									event.getWorld().getBlockState(event.getPos())).getItem())
								event.getWorld()
										.addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(),
												(ItemStack) drops.get(i)));
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(),
								SoundEvents.BLOCK_NETHER_WART_BREAK, SoundCategory.BLOCKS, 1.0F,
								0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), leaf.getDefaultState(), 2);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}
			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof GrapeBlock) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				if (!event.getWorld().isRemote) {
					drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
							(ServerWorld) event.getWorld(), event.getPos(),
							event.getWorld().getTileEntity(event.getPos()));
					for (int i = 0; i < drops.size(); i++) {
						event.getWorld().addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
								event.getPos().getY(), event.getPos().getZ(), (ItemStack) drops.get(i)));
					}
					event.getPlayer().addExhaustion(.05F);
					event.getWorld().playSound((PlayerEntity) null, event.getPos(),
							SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F,
							0.8F + event.getWorld().rand.nextFloat() * 0.4F);
					event.getWorld().setBlockState(event.getPos(), Blocks.AIR.getDefaultState(), 2);
				}
				event.getPlayer().swingArm(Hand.MAIN_HAND);

			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof WildPlant) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				WildPlant plant = (WildPlant) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (plant.isMaxAge(event.getWorld().getBlockState(event.getPos()))) {
					if (!event.getWorld().isRemote) {
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							drops.remove(0);
							event.getWorld().addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
									event.getPos().getY(), event.getPos().getZ(), (ItemStack) drops.get(i)));
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(), SoundEvents.BLOCK_GRASS_BREAK,
								SoundCategory.BLOCKS, 1.0F, 0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), plant.getDefaultState(), 2);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}

			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof NetherWartBlock) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				NetherWartBlock nether = (NetherWartBlock) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (event.getWorld().getBlockState(event.getPos()).get(NetherWartBlock.AGE) == 3) {
					if (!event.getWorld().isRemote) {
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							event.getWorld().addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
									event.getPos().getY(), event.getPos().getZ(), (ItemStack) drops.get(i)));
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(),
								SoundEvents.BLOCK_NETHER_WART_BREAK, SoundCategory.BLOCKS, 1.0F,
								0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), nether.getDefaultState(), 2);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}

			}

			if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof DoubleCrop) {
				if (!event.getPlayer().getHeldItemMainhand().isEmpty())
					event.setCanceled(true);
				DoubleCrop crop = (DoubleCrop) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (crop.getAge(event.getWorld().getBlockState(event.getPos())) == 7) {
					if (!event.getWorld().isRemote) {
						if (!event.getPlayer().getHeldItemMainhand().isEmpty())
							event.setCanceled(true);
						drops = Block.getDrops(event.getWorld().getBlockState(event.getPos()),
								(ServerWorld) event.getWorld(), event.getPos(),
								event.getWorld().getTileEntity(event.getPos()));
						for (int i = 0; i < drops.size(); i++) {
							if (drops.get(i).getItem() != getCropSeed(crop))
								event.getWorld()
										.addEntity(new ItemEntity((World) event.getWorld(), event.getPos().getX(),
												event.getPos().getY(), event.getPos().getZ(),
												(ItemStack) drops.get(i)));
						}
						event.getPlayer().addExhaustion(.05F);
						event.getWorld().playSound((PlayerEntity) null, event.getPos(), SoundEvents.BLOCK_CROP_BREAK,
								SoundCategory.BLOCKS, 1.0F, 0.8F + event.getWorld().rand.nextFloat() * 0.4F);
						event.getWorld().setBlockState(event.getPos(), Blocks.AIR.getDefaultState(), 2);
						event.getWorld().setBlockState(event.getPos().down(), crop.getDefaultState(), 2);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
				}
			}

		}

		if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof DoubleCrop) {
			if (event.getPlayer().getHeldItemMainhand().getItem() == Items.BONE_MEAL) {
				DoubleCrop crop = (DoubleCrop) event.getWorld().getBlockState(event.getPos()).getBlock();

				if (crop.getAge(event.getWorld().getBlockState(event.getPos())) == 6
						&& event.getWorld().getBlockState(event.getPos().up()) == Blocks.AIR.getDefaultState()) {
					if (event.getWorld().isRemote) {
						;
						event.getWorld().setBlockState(event.getPos().up(),
								crop.getDefaultState().with(DoubleCrop.AGE, 7), 2);
						if (!event.getPlayer().isCreative())
							event.getPlayer().getHeldItem(Hand.MAIN_HAND).shrink(1);
						event.getPlayer().addExhaustion(.05F);
					}
					event.getPlayer().swingArm(Hand.MAIN_HAND);
					double d0 = (double) ((float) event.getPos().getX());
					double d1 = (double) ((float) event.getPos().getY());
					double d2 = (double) ((float) event.getPos().getZ());
					event.getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, d0 + 0.5F, d1 + 0.5F, d2, 0.0D, 0.0D,
							0.0D);
				}
			}

		}

	}
}