/**
 * ------------------------------------------------------------------------
 * Microsoft Paint
 * Level 3 programming project
 *
 * by Riley ########
 *
 *
 *
 *
 * ------------------------------------------------------------------------
 */


import com.formdev.flatlaf.FlatDarkLaf
import java.awt.*
import java.awt.event.*
import javax.swing.*


//=============================================================================================

// Setup 'location' class for use as map
class Location(val name: String, val description: String) {
    // Setup variables for items at location and locations to the N S E W
    var item: Item? = null
    var north: Location? = null
    var south: Location? = null
    var east: Location? = null
    var west: Location? = null
    var canGoPast: Boolean = true
    var unlockedBy: Item? = null

    // Functions to add connections to other locations
    fun addLocationNorth(location: Location) {
        if (north == null) {
            north = location
            location.addLocationSouth(this)
        }
    }
    fun addLocationSouth(location: Location) {
        if (south == null) {
            south = location
            location.addLocationNorth(this)
        }
    }
    fun addLocationEast(location: Location) {
        if (east == null) {
            east = location
            location.addLocationWest(this)
        }
    }
    fun addLocationWest(location: Location) {
        if (west == null) {
            west = location
            location.addLocationEast(this)
        }
    }
    fun unlockedBy(item: Item) {
        if (this.canGoPast) {
            this.canGoPast = false
            this.unlockedBy = item
        }
    }

    // Display what this location is connected to
    fun info() {
        println("Location name: $name.")
        if (north != null) {
            println("To the north is ${north!!.name}")
        } else { println("I have no location north.") }
        if (south != null) {
            println("To the south is ${south!!.name}")
        } else { println("I have no location south.") }
        if (east != null) {
            println("To the east is ${east!!.name}")
        } else { println("I have no location east.") }
        if (west != null) {
            println("To the west is ${west!!.name}")
        } else { println("I have no location west.") }
        // Display items at location
        if (item != null) {
            println("I have ${item!!.name}")
        } else { println("I have no item.") }
        if (!canGoPast) {
            println("You cannot go past me. I'm unlocked by ${unlockedBy!!.name}")
        } else {println("You can go past me.")}
        println("---------------------------------------")
    }

}
class Item(val name: String) {
    var located: Location? = null

    fun addItemTo(location: Location) {
        if (location != null) {
            location.item = this
            located = location
        }
    }


}


/**
 * GUI class
 * Defines the UI and responds to events
 */
class GUI : JFrame(), ActionListener {
    // Create list of map locations
    val locations = mutableListOf<Location>()
    var currentLocation: Location
    var previousLocation: Location? = null

    val inventory = mutableListOf<Item>()

    // Setup some properties to hold the UI elements

    // Current Location Display
    private lateinit var currentLocationLabel: JLabel
    private lateinit var locationDescriptionLabel: JLabel
    private lateinit var locationImageLabel: JLabel

    // Movement Buttons
    private lateinit var goNorthButton: JButton
    private lateinit var goSouthButton: JButton
    private lateinit var goEastButton: JButton
    private lateinit var goWestButton: JButton

    // Inventory Display
    private lateinit var inventoryDisplayLabel: JLabel

    private lateinit var unlockLocationButton: JButton


    /**
     * Create, build and run the UI
     */
    init {
        // Create locations, map, items
        setupGame()
        // Create GUI
        setupWindow()
        buildUI()

        // Show the app, centred on screen
        setLocationRelativeTo(null)
        isVisible = true

        currentLocation = locations.first()
        showLocation()
    }
    private fun setupGame() {
        // Create locations
        val mountains = Location("Mountains", "It's really chilly here!")
        val plains = Location("Plains", "You can see so much of nothing.")
        val forest = Location("Forest", "Too much wood here!")
        val lake = Location("Lake", "What's that blue stuff?")
        val volcano = Location("Volcano", "Looks scary.")
        val door = Location("Door", "A foreboding door blocks the way.")
        val home = Location("Home", "It's where you live.")

        val key = Item("Golden Key")

        // Add locations to mutable list
        locations.add(plains)
        locations.add(mountains)
        locations.add(forest)
        locations.add(lake)
        locations.add(volcano)
        locations.add(door)

        // Create map by connecting locations to each other
        plains.addLocationNorth(mountains)
        plains.addLocationSouth(volcano)
        plains.addLocationWest(forest)
        plains.addLocationEast(lake)
        mountains.addLocationNorth(door)
        door.addLocationWest(home)
        key.addItemTo(lake)
        door.unlockedBy(key)


        // debug show map info
        mountains.info()
        plains.info()
        forest.info()
        lake.info()
        volcano.info()
        door.info()


    }

    /**
     * Configure the main window
     */
    private fun setupWindow() {
        title = "MS Paint Escape"
        contentPane.preferredSize = Dimension(1000, 750)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null

        pack()
    }

    /**
     * Populate the UI
     */
    private fun buildUI() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

//        exampleLabel = JLabel("Hello, World!", SwingConstants.CENTER)
//        exampleLabel.bounds = Rectangle(30, 30, 240, 40)
//        exampleLabel.font = baseFont
//        add(exampleLabel)

        // consider updating the labels to textareas > https://stackoverflow.com/questions/40811364/how-to-wrap-text-in-a-jlabel

        // Location Displays
        // should this be centered or to the left?
        currentLocationLabel = JLabel("CURRENT LOCATION", SwingConstants.LEFT)
        currentLocationLabel.bounds = Rectangle(259, 10, 735, 56)
        currentLocationLabel.font = baseFont
        add(currentLocationLabel)


        locationDescriptionLabel = JLabel("CURRENT LOCATION DESCRIPTION", SwingConstants.LEFT)
        locationDescriptionLabel.setVerticalAlignment(SwingConstants.TOP)
        locationDescriptionLabel.bounds = Rectangle(259, 66, 735, 181)
        locationDescriptionLabel.font = baseFont
        add(locationDescriptionLabel)

        // make width and height equal, add margin/padding
        // need to make this image change based on location > new variable/class?
        var locationImage = ImageIcon("src/images/amogg us.png").image
        locationImage = locationImage.getScaledInstance(253,237, Image.SCALE_SMOOTH)
        locationImageLabel = JLabel()
        locationImageLabel.bounds = Rectangle(6, 10, 253, 237)
        locationImageLabel.icon = ImageIcon(locationImage)
        add(locationImageLabel)

        // Inventory display > consider adding a popup "you found [x]!"
        // this ones a piece of garbage and hates swingconstants in the label itself
        inventoryDisplayLabel = JLabel("CURRENT INVENTORY")
        inventoryDisplayLabel.setVerticalAlignment(SwingConstants.TOP)
        inventoryDisplayLabel.setHorizontalAlignment(SwingConstants.LEFT)
        inventoryDisplayLabel.bounds = Rectangle(6, 247, 253, 490)
        inventoryDisplayLabel.font = baseFont
        add(inventoryDisplayLabel)




        //---NAVIGATION BUTTONS CENTER DIS SHIT BRUH ---------------------------------------------------------------------------------------
        goNorthButton = JButton("CURRENT LOCATION NORTH")
        goNorthButton.bounds = Rectangle(535,297,183,115)
        goNorthButton.font = baseFont
        goNorthButton.addActionListener(this)
        add(goNorthButton)

        goSouthButton = JButton("CURRENT LOCATION SOUTH")
        goSouthButton.bounds = Rectangle(535,573,183,115)
        goSouthButton.font = baseFont
        goSouthButton.addActionListener(this)
        add(goSouthButton)

        goEastButton = JButton("CURRENT LOCATION EAST")
        goEastButton.bounds = Rectangle(718,435,183,115)
        goEastButton.font = baseFont
        goEastButton.addActionListener(this)
        add(goEastButton)

        goWestButton = JButton("CURRENT LOCATION WEST")
        goWestButton.bounds = Rectangle(352,435,183,115)
        goWestButton.font = baseFont
        goWestButton.addActionListener(this)
        add(goWestButton)

        unlockLocationButton = JButton("UNLOCK CURRENT LOCATION")
        unlockLocationButton.bounds = Rectangle(555, 445, 143, 95)
        unlockLocationButton.font = baseFont
        unlockLocationButton.addActionListener(this)
        add(unlockLocationButton)



    }

    /**
     * Handle any UI events
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            goNorthButton -> goNorth()
            goSouthButton -> goSouth()
            goEastButton -> goEast()
            goWestButton -> goWest()
            unlockLocationButton -> unlockLocation()
        }
    }


    private fun showLocation() {
        // Enable buttons if valid movement allowed
        goNorthButton.isEnabled = (currentLocation.north != null)
                && (currentLocation.canGoPast || currentLocation.north == previousLocation)
        goSouthButton.isEnabled = (currentLocation.south != null)
                && (currentLocation.canGoPast || currentLocation.south == previousLocation)
        goEastButton.isEnabled = (currentLocation.east != null)
                && (currentLocation.canGoPast || currentLocation.east == previousLocation)
        goWestButton.isEnabled = (currentLocation.west != null)
                && (currentLocation.canGoPast || currentLocation.west == previousLocation)

        // Show unlock location button if location is locked
        if (!currentLocation.canGoPast) {
            unlockLocationButton.isVisible = true
            unlockLocationButton.text = "<html>Use ${currentLocation.unlockedBy!!.name}"
            if (inventory.any{it == currentLocation.unlockedBy}) { // Allow use of unlock button if player has required item
                unlockLocationButton.isEnabled = true
            }
        }
        else {
            // Hide unlock location button > should it be shown, disabled and say "no item to use"?
            unlockLocationButton.isVisible = false
            unlockLocationButton.isEnabled = false
        }

        // Update display labels to show current location + description
        currentLocationLabel.text = currentLocation.name
        locationDescriptionLabel.text = "<html>" + currentLocation.description


        // Update travel buttons to show locations connected to current location > you can make the text display
        // more efficient if you produce <html><div style='text-align: center;'>[direction] and then add a conditional
        // "if X != null" then + currentlocation.[direction]!!.name else + "nothing" > cleaner code!
        if (currentLocation.north != null) {
            goNorthButton.text = "<html><div style='text-align: center;'>North<br>" + currentLocation.north!!.name
        } else { goNorthButton.text = "<html><div style='text-align: center;'>North<br>Nothing</html>" }

        if (currentLocation.south != null) {
            goSouthButton.text = "<html><div style='text-align: center;'>South<br>" + currentLocation.south!!.name
        } else { goSouthButton.text = "<html><div style='text-align: center;'>South<br>Nothing</html>" }

        if (currentLocation.east != null) {
            goEastButton.text = "<html><div style='text-align: center;'>East<br>" + currentLocation.east!!.name
        } else { goEastButton.text = "<html><div style='text-align: center;'>East<br>Nothing</html>" }

        if (currentLocation.west != null) {
            goWestButton.text = "<html><div style='text-align: center;'>West<br>" + currentLocation.west!!.name
        } else { goWestButton.text = "<html><div style='text-align: center;'>West<br>Nothing" }

        updateInventory()


    }

    /**
     *  Movement Functions
     * Perform error check, if valid change location in {direction} to current location
     */
    private fun goNorth() {
        // Error check => update north to current location
        if (currentLocation.north != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.north!!
            showLocation()
        }
    }

    private fun goSouth() {
        // Error check => update south to current location
        if (currentLocation.south != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.south!!
            showLocation()
        }
    }

    private fun goEast() {
        // Error check => update east to current location
        if (currentLocation.east != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.east!!
            showLocation()
        }
    }

    private fun goWest() {
        // Error check => update west to current location
        if (currentLocation.west != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.west!!
            showLocation()
        }
    }

    /**
     * Inventory Functions
     * Display and update player inventory
     */

    private fun updateInventory() {
        if (currentLocation.item != null) {
            inventory.add(currentLocation.item!!)
            currentLocation.item = null
        }


        var textUpdate = "<html>"
        inventory.forEach() {
            textUpdate += "<li>${it.name}</li><br>"
        }
        if (textUpdate != "<html>") {
            inventoryDisplayLabel.text = textUpdate
        }
        else {
            inventoryDisplayLabel.text = "You have no items."
        }
    }

    private fun unlockLocation() {
        while (!currentLocation.canGoPast) {
            inventory.forEach() {
                if (it == currentLocation.unlockedBy) {
                    inventory.remove(it)
                    currentLocation.unlockedBy = null
                    currentLocation.canGoPast = true
                    showLocation()
                    return
                }
            }


        }
    }
}


//=============================================================================================

/**
 * Launch the application
 */
fun main() {
    // Flat, Dark look-and-feel
    FlatDarkLaf.setup()

    // Create the UI
    GUI()
}


