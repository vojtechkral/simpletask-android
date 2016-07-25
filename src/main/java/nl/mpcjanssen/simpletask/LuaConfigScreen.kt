/**
 * @copyright 2014- Mark Janssen)
 */
package nl.mpcjanssen.simpletask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import nl.mpcjanssen.simpletask.util.Config
import nl.mpcjanssen.simpletask.util.createAlertDialog
import org.luaj.vm2.LuaError

class LuaConfigScreen : ThemedActivity() {

    private val log = Logger
    private lateinit var m_app : TodoApplication
    private lateinit var scriptEdit : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m_app = application as TodoApplication
        setContentView(R.layout.lua_config)
        scriptEdit = findViewById(R.id.lua_config) as EditText
        val btnRun = findViewById(R.id.btn_run) as Button
        val btnHelp = findViewById(R.id.btn_help) as Button
        btnRun.setOnClickListener {
            try {
                Config.interp.evalScript(script())
            } catch (e: LuaError) {
                log.debug(FilterScriptFragment.TAG, "Lua execution failed " + e.message)
                createAlertDialog(this, R.string.lua_error, e.message ?: "").show()
            }
        }
        btnHelp.setOnClickListener {
            val intent = Intent(this, HelpScreen::class.java)
            intent.putExtra(Constants.EXTRA_HELP_PAGE, "script")
            startActivityForResult(intent, 0)
        }
        scriptEdit.setText(Config.luaConfig)
    }

    fun script () : String {
        return scriptEdit.text.toString()
    }

    override fun onDestroy() {
        Config.luaConfig = script()
        Config.reloadLuaConfig()
        super.onDestroy()
    }

    companion object {
        internal val TAG = LuaConfigScreen::class.java.simpleName
    }
}
