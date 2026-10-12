package top.wkbin.taixu.ui.components

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.alexzhirkevich.cupertino.icons.CupertinoIcons
import io.github.alexzhirkevich.cupertino.icons.outlined.*
import top.wkbin.taixu.ui.components.symbols.outlined.*
import io.github.alexzhirkevich.cupertino.icons.filled.House as FilledHouse
import io.github.alexzhirkevich.cupertino.icons.filled.Gearshape as FilledGearshape
import top.wkbin.taixu.ui.components.symbols.filled.BubbleLeft as FilledBubbleLeft
import top.wkbin.taixu.ui.components.symbols.filled.Folder as FilledFolder

internal fun chengmingSymbol(name: RuntimeIconName): ImageVector? = with(CupertinoIcons.Outlined) {
    when (name) {
        RuntimeIconName.Cpu -> Cpu
        RuntimeIconName.Storage, RuntimeIconName.Save -> Externaldrive
        RuntimeIconName.Terminal -> Terminal
        RuntimeIconName.Brain, RuntimeIconName.Model, RuntimeIconName.Bot -> Brain
        RuntimeIconName.Palette -> Paintpalette
        RuntimeIconName.Server -> ServerRack
        RuntimeIconName.Package -> Shippingbox
        RuntimeIconName.Shield, RuntimeIconName.Admin -> Shield
        RuntimeIconName.Sparkles -> Sparkles
        RuntimeIconName.Extension -> PuzzlepieceExtension
        RuntimeIconName.Home, RuntimeIconName.NavDashboard -> House
        RuntimeIconName.Chat, RuntimeIconName.NavMessage -> BubbleLeft
        RuntimeIconName.Folder, RuntimeIconName.Workspace, RuntimeIconName.NavRepository -> Folder
        RuntimeIconName.Settings, RuntimeIconName.NavSettings -> Gearshape
        RuntimeIconName.Back -> ChevronBackward
        RuntimeIconName.ChevronRight -> ChevronForward
        RuntimeIconName.ChevronDown -> ChevronDown
        RuntimeIconName.ChevronUp -> ChevronUp
        RuntimeIconName.Refresh -> ArrowClockwise
        RuntimeIconName.Search -> MagnifyingGlass
        RuntimeIconName.More -> Ellipsis
        RuntimeIconName.Plus -> Plus
        RuntimeIconName.Close -> Xmark
        RuntimeIconName.Check -> Checkmark
        RuntimeIconName.Play -> Play
        RuntimeIconName.Trash -> Trash
        RuntimeIconName.List, RuntimeIconName.Logs -> ListBullet
        RuntimeIconName.OpenInNew -> SquareAndArrowUp
        RuntimeIconName.Info -> InfoCircle
        RuntimeIconName.Alert -> ExclamationmarkTriangle
        RuntimeIconName.Edit -> Pencil
        RuntimeIconName.Mail -> Envelope
        RuntimeIconName.Phone -> Phone
        else -> null
    }
}

internal fun chengmingTabSymbol(destination: MainDestination, selected: Boolean): ImageVector =
    if (selected) with(CupertinoIcons.Filled) {
        when (destination) {
            MainDestination.Home -> FilledHouse
            MainDestination.Agent -> FilledBubbleLeft
            MainDestination.Workspace -> FilledFolder
            MainDestination.Settings -> FilledGearshape
        }
    } else chengmingSymbol(destination.icon)!!
