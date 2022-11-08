package xyz.unitedrhythmizedclub.fmd.ui

import xyz.unitedrhythmizedclub.fmd.FMDMain
import java.awt.Dimension
import java.awt.Point
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import javax.swing.*
import kotlin.Exception

class FMDUI : JFrame("ForgeModDeobfuscator") {
    init {
        size = Dimension(450, 110)
        isVisible = true
        defaultCloseOperation = 3
        isResizable = false
        addKeyListener(KeyHandler())

        val input = JTextField()
        val startBtn = JButton("Start")
        val browseBtn = JButton("Browse")
        val comboBox = JComboBox<File>()

        startBtn.size = Dimension(100, 30)
        startBtn.location = Point(450 - 5 - 115, 35)
        add(startBtn)
        startBtn.addActionListener {
            Thread({
                try {
                    FMDMain.process(comboBox.getItemAt(comboBox.selectedIndex), File(input.text))
                } catch (e: Exception) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Failed to deobfuscate mod file.",
                        "ForgeModDeobfuscator",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }, "FMD-Thread").start()
        }

        input.size = Dimension(325, 30)
        input.location = Point(5, 5)
        add(input)

        browseBtn.size = Dimension(100, 30)
        browseBtn.location = Point(450 - 5 - 115, 5)
        add(browseBtn)

        browseBtn.addActionListener {
            val browser = JFileChooser()
            if (browser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION) {
                input.text = browser.selectedFile.absolutePath
            }
        }

        input.dropTarget = object : DropTarget() {
            override fun drop(dtde: DropTargetDropEvent?) {
                try {
                    dtde?.acceptDrop(DnDConstants.ACTION_COPY)
                    @Suppress("UNCHECKED_CAST")
                    val files: List<File> =
                        dtde?.transferable?.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                    for (file in files) {
                        input.text = file.canonicalPath
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        for (file in FMDMain.mappingsFiles) {
            comboBox.addItem(file)
        }
        comboBox.size = Dimension(325, 30)
        comboBox.location = Point(5, 35)
        add(comboBox)

        repaint()
        layout = null
    }
}

class KeyHandler : KeyListener {

    override fun keyTyped(e: KeyEvent?) {

    }

    override fun keyPressed(e: KeyEvent) {
        val isRepaintKeyPressed = e.keyCode == KeyEvent.VK_F5
        if (isRepaintKeyPressed) {
            if (e.source is JFrame) {
                (e.source as JFrame).repaint()
                FMDMain.logger.debug("Successfully repainted JFrame.")
            } else {
                FMDMain.logger.debug(e.source.javaClass.name)
            }
        }

    }

    override fun keyReleased(e: KeyEvent?) {

    }

}