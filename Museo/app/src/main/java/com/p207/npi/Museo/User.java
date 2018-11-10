package com.p207.npi.Museo;

    import android.graphics.Bitmap;
    import android.support.annotation.NonNull;

    import com.github.bassaer.chatmessageview.model.IChatUser;

    import org.jetbrains.annotations.NotNull;
    import org.jetbrains.annotations.Nullable;

public class User implements IChatUser {
    private Integer id;
    private String name;
    private Bitmap icon;

    User(int id, String name, Bitmap icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getId() {
        return this.id.toString();
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public Bitmap getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(@NonNull Bitmap icon) {
        this.icon = icon;
    }
}