package main.java.BlackBeltPanda.EssentialsKitsToCMI;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.Zrips.CMI.CMI;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;

public class EssentialsKitsToCMI extends JavaPlugin {
    
    @Override
    public void onEnable() {
        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        CMI cmi = CMI.getInstance();
            try {
                final String[] kitList = Objects.requireNonNull(essentials).getKits().listKits(essentials, null).split(" ");
                for (final String kitName : kitList) {
                    if (!kitName.isEmpty()) {
                        Kit essentialsKit = new Kit(kitName, essentials);
                        Map<String,Object> essKitData = essentials.getKits().getKit(essentialsKit.getName());
                        com.Zrips.CMI.Modules.Kits.Kit cmiKit = new com.Zrips.CMI.Modules.Kits.Kit(kitName);
                        List<String> commands = new ArrayList<>();
                        ItemStack icon = null;
                        for (int i = 0; i < essentialsKit.getItems().size(); i++) {
                            String kitItem = essentialsKit.getItems().get(i);
                            // Convert placeholders
                            kitItem = kitItem.replaceAll("\\{player\\}", "\\{USERNAME\\}");
                            // Money
                            boolean currencyIsSuffix = essentials.getConfig().getBoolean("currency-symbol-suffix", false);
                            if (!currencyIsSuffix ? kitItem.startsWith(essentials.getSettings().getCurrencySymbol()) : kitItem.endsWith(essentials.getSettings().getCurrencySymbol())) {
                                final String valueString = sanitizeCurrencyString(kitItem, essentials.getSettings().getCurrencySymbol(), currencyIsSuffix);
                                commands.add("cmi money pay {USERNAME} " + valueString.trim());
                                continue;
                            }
                            
                            // Commands
                            if (kitItem.startsWith("/")) {
                                commands.add(kitItem.substring(1));
                                continue;
                            }
                            
                            // Skip borked items
                            if (kitItem.startsWith("unknown")) {
                                continue;
                            }
                            
                            // Items
                            final String[] parts = kitItem.split(" +");
                            ItemStack parseStack;
                            try {
                                parseStack = essentials.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                            } catch (Exception e) {
                                continue;
                            }
                            
                            if (parseStack.getType() == Material.AIR) {
                                continue;
                            }
                            
                            final MetaItemStack metaItemStack = new MetaItemStack(parseStack);
                            
                            if (parts.length > 2) {
                                try {
                                    metaItemStack.parseStringMeta(null,  essentials.getSettings().allowUnsafeEnchantments(), parts, 2, essentials);
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                            
                            cmiKit.setItem(i, metaItemStack.getItemStack());
                            if (icon == null) icon = metaItemStack.getItemStack();
                        }
                        
                        // Set all the options
                        cmiKit.setCommands(commands);
                        long delay = essKitData.containsKey("delay") ? ((Number) essKitData.get("delay")).longValue() : 0L;
                        cmiKit.setDelay(delay >= 0 ? delay : 0L);
                        cmiKit.setName(essentialsKit.getName());
                        cmiKit.setCommandName(essentialsKit.getName());
                        cmiKit.setEnabled(true);
                        cmiKit.setDropItems(essentials.getSettings().isDropItemsIfFull());
                        cmiKit.setMaxUsages(delay < 0 ? 1 : -1);
                        cmiKit.setIconOff(new ItemStack(Material.BARRIER));
                        cmiKit.setIcon((icon != null && icon.getType() != Material.AIR) ? icon : new ItemStack(Material.STONE));
                        cmi.getKitsManager().addKit(cmiKit);
                    }
                }
                cmi.getKitsManager().safeSave();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    private String sanitizeCurrencyString(String input, String symbol, boolean suffix) {
        if (input.contains(symbol)) {
            return suffix ? input.substring(0, input.indexOf(symbol)) : input.substring(symbol.length());
        }
        return input;
    }

}
