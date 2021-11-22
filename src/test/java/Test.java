import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

/**
 * @Author xbaimiao
 * @Date 2021/11/7 19:02
 */
public class Test extends CraftPlayer {
    public Test(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (mode.equals(GameMode.CREATIVE)) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            int index = 0;
            while (index < elements.length && elements[index].toString().contains("getStackTrace")) {
                index++;
            }
            while (index < elements.length && elements[index].toString().contains("setGameMode")) {
                index++;
            }
            String stack = elements[index].toString();
            String stringBuilder = "[BANOP]§f已阻止插件调用创造模式方法(player: " + this.getName() + ") §a->§f " + stack;
            Bukkit.getLogger().info(stringBuilder);
            return;
        }
    }

}
