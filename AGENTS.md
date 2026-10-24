# AGENTS.md — SlimefunInfiniteBlock

给接手本仓库的 AI / 协作者看的实操指南。先读再改，别凭直觉动 Slimefun 注册顺序。

## 1. 项目是什么

Slimefun4 的 Minecraft 附属插件（PaperMC 1.20.1）。核心玩法：把任意方块（原版或 Slimefun 物品）"融合"成可无限使用/可计数的特殊物品。

插件名：`SlimefunInfiniteBlocks`，版本 `1.0.1`（见 `src/main/resources/plugin.yml` 的 `${project.version}`）。
作者：TouRuya、balugaq。Issue 跟踪：<https://github.com/balugaq/SlimefunInfiniteBlock/issues>

### 主要物品与功能
| 物品 | 位置 | 作用 |
|------|------|------|
| `Combiner` 融合机 | `implementation.items.Combiner` | 机器，把手中方块合成「无尽方块」物品 |
| `CombinedBlock` 无尽方块 | `implementation.items.CombinedBlock` | 右键放置，消耗数量；达阈值后真正无限 |
| `InfiniteStar` 无限之星 | `implementation.items.InfiniteStar` | 记录"你挖过某方块 N 次"的星物品 |
| `StarOperator` 星之算子 | `implementation.items.StarOperator` | 机器，对方块产出星 |
| `ItemFountainBalancer` / `ENItemFountainBalancer` | `implementation.items.*` | 物品爆发/平衡机器（EN 为变体） |

### 关键依赖与前置
- 必须前置：`Slimefun`（plugin.yml `depend`）。
- 软前置：`Networks`、`Networks-Changed`（`softdepend`，可选，缺失不崩）。
- Java 16，Maven 构建，Lombok 注解处理器。
- 关键三方库：`paper-api` 1.20.1、`Slimefun4` b39097e015（jitpack）、`bstats-bukkit` 3.0.2、`GuizhanLibPlugin` 1.7.6（中文本地化，provided）、`MorePersistentDataTypes` 2.4.0。

## 2. 构建与运行

```bash
mvn clean package      # pom.xml defaultGoal 即 clean package
```

- 产物经 `maven-shade-plugin`（3.6.0）打包，`minimizeJar=true`，`finalName` 形如 `SlimefunInfiniteBlock v1.0.1.jar`，位于 `target/`。
- 因 `minimizeJar`，不要把用不到的库类当成"有/无"来判断逻辑——按需即可。
- 运行需把 jar 放进开了 Slimefun 的 Paper 1.20.1 服务端 `plugins/`。

## 3. 目录结构与各层职责

```
src/main/java/net/touruya/infiniteblock/
├── implementation/            # 启动入口 + 物品/分组/配方注册
│   ├── InfiniteBlocks.java     # 主类 (JavaPlugin + SlimefunAddon)
│   ├── SIBGroups.java          # 物品分组（materials / machines / infinite_blocks）
│   ├── SIBItems.java           # 所有 SlimefunItem 实例的创建与 register
│   ├── SIBRecipeTypes.java     # 自定义配方类型（combiner / break_block）
│   └── items/                  # 各物品的具体类
├── core/
│   ├── commands/              # /sibstorage、/sibstar 指令实现
│   ├── listeners/            # BlockListener / StarListener / CraftListener
│   └── managers/             # ConfigManager / ListenerManager / PlayerDataManager
├── api/
│   ├── objects/              # CustomSlimefunItem / CustomAContainer / SubCommand（基类）
│   └── stored/               # Stored 抽象 + 实现 + NotDupeable 标记
└── utils/                    # Constants / Icons / StoredUtils / StackUtils /
                              # PermissionUtil / SlimefunItemUtil / BlockMenuUtil
```

分层原则：
- `api/` 是对外/对内的抽象基类与可复用类型，改动要谨慎、保持向后兼容。
- `implementation/` 是接线（wiring）层：把所有物品、分组、配方、监听器、指令组装起来。
- `core/` 是插件内部机制（命令、事件、数据）。
- `utils/` 是无状态工具，依赖 `Constants` 拿全局单例。

## 4. 启动顺序（onEnable 不可乱序）

`InfiniteBlocks.onEnable()` 顺序（见 `implementation/InfiniteBlocks.java`）：

1. `instance = this`（全局单例）。
2. `new ConfigManager` → 写默认 config.yml。
3. `new PlayerDataManager(this, Constants.DATA_FILE)` → 加载 `data/data.yml`。
4. `SIBGroups.setup` → `SIBRecipeTypes.setup` → `SIBItems.setup`（**必须先建分组/配方类型，再建物品**）。
5. `ListenerManager.setup()` → 注册 3 个监听器。
6. 注册指令 `sibstorage`、`sibstar`。
7. 启动异步自动保存任务：`runTaskTimerAsynchronously`，周期 `Constants.AUTOSAVE_PERIOD * 20` tick。

`onDisable`：先 `saveData()`，再 `SlimefunItemUtil.unregisterAllItems()` / `unregisterAllItemGroups()`。

## 5. 核心数据模型（改物品前必读）

**无尽方块（CombinedBlock）的 PDC 字段**（`utils/Constants.java` 定义 NamespacedKey）：
- `stored`（STRING）：物品标识，格式 `vanilla:STONE` 或 `slimefun:<sfId>`。
- `stored_amount`（LONG）：剩余数量。

**无限之星（InfiniteStar）的 PDC 字段**：
- `identifier`（STRING）：同上的物品标识。

**"无限"判定有两套口径，改动时要当心**：
- `Constants.INFINITY_THRESHOLD` = `ConfigManager.getInfinityThreshold()`，来自 config `infinity-threshold`（默认 `1000000`）。
- `StoredUtils.isInfinity(...)` 判定的是 `amount >= Long.MAX_VALUE / 2`。
两者阈值不同，涉及"何时算无限"的逻辑需同时核对这两处，避免不一致。

**`Stored` 抽象**（`api/stored/`）：无尽方块/星内部存的是什么方块，统一走 `Stored` 接口（`getName` / `getItemStack` / `getIdentifier`）。
- `VanillaStored`：原版 Material。
- `SlimefunStored`：Slimefun 物品，identifier 形如 `slimefun:<id>`。
- `NotDupeable`：标记接口（`CombinedBlock`、`InfiniteStar` 实现），用于防止物品被复制/刷物品。新增可无限使用的物品应评估是否实现它。
- `StoredUtils` 是读写 PDC、构造/解包物品、刷新 lore 的集中工具类。

**玩家数据（`data/data.yml`，由 `PlayerDataManager` 管）**：
```
player_name:
  count:                 # 累计破坏数量
    <sfId 或 material>: int
  get:                   # 已获得无限之星数量
    <sfId 或 material>: int
```
注意：`PlayerDataManager` 在内存持有一份 `YamlConfiguration snapshot`，所有 set/add 都改它；只有 `saveData()`（自动保存任务 + onDisable）才落盘。崩溃会丢未保存增量。

## 6. 配置项（`src/main/resources/config.yml`）

| key | 含义 | 默认值 |
|-----|------|--------|
| `infinity-threshold` | 无尽方块视为无限的最低堆叠数 | `1000000` |
| `autosave-period` | 自动保存周期（秒） | `300` |
| `star-threshold` | 每破坏多少方块掉落 1 个无限之星 | `100000` |

`ConfigManager` 只在新键缺失时回填默认值（不覆盖用户已有配置）。改默认值去 `config.yml`，别只改代码里的 fallback。

## 7. 指令与权限

| 指令 | 别名 | 类 | 权限 | 默认 |
|------|------|----|------|------|
| `/sibstorage` | `slimefuninfiniteblockstorage` | `core.commands.StorageCommand` | `slimefuninfiniteblock.command.storage` | op |
| `/sibstar` | `slimefuninfiniteblockstar` | `core.commands.StarCommand` | `slimefuninfiniteblock.command.star` | op |
| — | — | — | `slimefuninfiniteblock.admin` / `.command.admin` | op |

- `StorageCommand`：手持一个方块，输入数量，获得对应「无尽方块」物品。
- `StarCommand`：见 `core.commands.StarCommand`（代码内有 `todo`：新增查看/设置玩家已挖方块数的子指令）。
- 新增指令：继承 `api.objects.SubCommand`，并在 `InfiniteBlocks.onEnable` 注册（参考现有两指令）。

## 8. 监听器（`core/listeners/`，统一由 `ListenerManager` 注册）

- `BlockListener.onCombinedBlockPlace`：右键手持 `CombinedBlock` 放置 → 放置方块、扣数量、放烟花粒子；若是 `SlimefunStored` 还会触发其 `BlockPlaceHandler`。放置走 `runTaskLater(..., 1L)` 延迟一 tick 执行。
- `StarListener.countBlock`（BlockBreakEvent）：累计玩家破坏数，每达 `star-threshold` 的倍数发 1 个 `InfiniteStar`。`uncountBlock`（BlockPlaceEvent）：放回方块时扣减计数。
- `CraftListener`：处理合成相关逻辑（含对 `NotDupeable` 物品的保护）。改动复制/刷物品防护时优先看这里。

## 9. 开发约定与坑

- 全部物品继承 `api.objects.CustomSlimefunItem`（再往下是 `NotDupeable` 等）。机器类用 `CustomAContainer`。
- 物品实例集中在 `SIBItems.setup()` 创建并 `register(plugin)`；新增物品只改这一处 + `SIBGroups`/`SIBRecipeTypes`，不要散落注册。
- 全局单例统一走 `Constants.PLUGIN`（`InfiniteBlocks.getInstance()`）或各 Manager 的 `instance()` 静态方法；不要自己 `new`。
- 大量使用 Lombok（`@Getter`、`@UtilityClass`、`@ParametersAreNonnullByDefault` 等），编译依赖注解处理器，IDE 需装 Lombok 插件。
- 中文注释与中文玩家提示是项目惯例，新增提示保持中文。
- 不要提交构建产物（`target/`）；本仓库 `.workbuddy` 等本地数据不进提交。
- 改动涉及 PDC key、数据文件结构、Slimefun 注册顺序时，先确认向后兼容，再动手。

## 10. 常见改动落点速查

- 加新物品 → `SIBItems.java` + `implementation/items/` 新建类 + 视情况加 `SIBGroups`/`SIBRecipeTypes`。
- 改无限阈值/保存周期 → `config.yml` + `ConfigManager` 的 getter + `Constants`。
- 改玩家数据统计 → `StarListener` + `PlayerDataManager`。
- 改指令 → `core/commands/` + `InfiniteBlocks.onEnable` 注册。
- 改 PDC 读写 → `StoredUtils` + `Constants` 的 NamespacedKey。
- 防刷物品 → `NotDupeable` + `CraftListener`。
