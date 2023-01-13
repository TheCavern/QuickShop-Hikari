package com.ghostchu.quickshop.util.paste.item;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.util.FastPlayerFinder;
import com.ghostchu.quickshop.util.paste.GuavaCacheRender;
import com.google.common.cache.CacheStats;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

public class CachePerformanceItem implements SubPasteItem {
    private final QuickShop plugin = QuickShop.getInstance();

    @NotNull
    private String buildPAPICacheContent() {
        if (plugin.getPlaceHolderAPI() == null || plugin.getQuickShopPAPI() == null) {
            return "<p>PlaceHolderAPI feature disabled.</p>";
        }
        if (!plugin.getQuickShopPAPI().isRegistered()) {
            return "<p>PlaceHolderAPI feature not registered yet.</p>";
        }
        if (plugin.getQuickShopPAPI().getPapiCache() == null) {
            return "<p>PlaceHolderAPI Cache disabled.</p>";
        }
        CacheStats stats = plugin.getQuickShopPAPI().getPapiCache().getStats();
        return renderTable(stats);
    }

    @NotNull
    private String buildShopCacheContent() {
        if (plugin.getShopCache() == null) {
            return "<p>Shop Cache disabled.</p>";
        }
        CacheStats stats = plugin.getShopCache().getStats();
        return renderTable(stats);
    }

    @Override
    public @NotNull String genBody() {
        return "<h5>Shop Cache</h5>" +
                buildShopCacheContent() +
                "<h5>PlaceHolderAPI Cache</h5>" +
                buildPAPICacheContent() +
                "<h5>Player Lookup Cache</h5>" +
                buildPlayerLookupCache();
    }

    private String buildPlayerLookupCache() {
        CacheStats stats = ((FastPlayerFinder) plugin.getPlayerFinder()).getNameCache().stats();
        return renderTable(stats);
    }

    @Override
    public @NotNull String getTitle() {
        return "Cache Performance";
    }

    @NotNull
    private String renderTable(@NotNull CacheStats stats) {
        return GuavaCacheRender.renderTable(stats);
    }

    @NotNull
    private String round(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        return nf.format(d);
    }
}
