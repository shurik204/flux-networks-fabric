package sonar.fluxnetworks.client.design;

import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.Spanned;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import sonar.fluxnetworks.common.connection.FluxNetwork;
import sonar.fluxnetworks.common.util.FluxUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An input filter for network password.
 *
 * @see icyllis.modernui.widget.TextView#setFilters(InputFilter[])
 */
@Environment(EnvType.CLIENT)
public class PasswordFilter extends InputFilter.LengthFilter {

    private static final PasswordFilter sInstance = new PasswordFilter();

    private PasswordFilter() {
        super(FluxNetwork.MAX_PASSWORD_LENGTH);
    }

    @Nonnull
    public static PasswordFilter getInstance() {
        return sInstance;
    }

    @Nullable
    @Override
    public CharSequence filter(@Nonnull CharSequence source, int start, int end,
                               @Nonnull Spanned dest, int dstart, int dend) {
        final CharSequence superResult = super.filter(source, start, end, dest, dstart, dend);
        if (superResult != null) {
            // Filtered by the super class.
            return superResult;
        }

        int i;
        for (i = start; i < end; i++) {
            // All chars are BMP.
            if (FluxUtils.isBadPasswordChar(source.charAt(i))) {
                break;
            }
        }

        if (i == end) {
            // It was all OK.
            return null;
        }

        if (end - start == 1) {
            // It was not OK, and there is only one char, so nothing remains.
            return "";
        }

        StringBuilder filtered = new StringBuilder();
        filtered.append(source, start, end);
        i -= start;
        end -= start;

        // Only count down to i because the chars before that were all OK.
        for (int j = end - 1; j >= i; j--) {
            if (FluxUtils.isBadPasswordChar(source.charAt(j))) {
                filtered.delete(j, j + 1);
            }
        }

        return filtered;
    }
}
