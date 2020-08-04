package com.sahilmehra.expensemanager.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.sahilmehra.expensemanager.R
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : Fragment() {
    private lateinit var tilUserName: TextInputLayout
    private lateinit var tilMonthlyBudget: TextInputLayout
    private lateinit var tilMonthlyIncome: TextInputLayout

    private lateinit var tietUserName: EditText
    private lateinit var tietMonthlyBudget: EditText
    private lateinit var tietMonthlyIncome: EditText

    private var userName = ""
    private var monthlyBudget = 1000F
    private var monthlyIncome = 1000F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireActivity().getSharedPreferences("EXPENSE_MANAGER", Context.MODE_PRIVATE)
        val previouslyStarted = sharedPreferences.getBoolean("PREVIOUSLY_STARTED", false)

        if (previouslyStarted)
            findNavController().navigate(R.id.action_onboarding_to_tab)

        tietUserName = view.findViewById(R.id.tietuserName)
        tietMonthlyBudget = view.findViewById(R.id.tietMonthlyBudget)
        tietMonthlyIncome = view.findViewById(R.id.tietMonthlyIncome)

        tilUserName = view.findViewById(R.id.tilUserName)
        tilMonthlyBudget = view.findViewById(R.id.tilMonthlyBudget)
        tilMonthlyIncome = view.findViewById(R.id.tilMonthlyIncome)

        validateText(tietUserName, tilUserName)
        validateText(tietMonthlyBudget, tilMonthlyBudget)
        validateText(tietMonthlyIncome, tilMonthlyIncome)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnContinue.setOnClickListener {
            checkValidations()
        }
    }

    private fun validateText(editText: EditText, textLayout: TextInputLayout) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty())
                    textLayout.error = "Required Field !"
                else
                    textLayout.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        editText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (editText.text.toString().isEmpty())
                    textLayout.error = "Required Field!"
                else
                    textLayout.error = null
            }
        }
    }

    private fun checkValidations() {
        if (tietUserName.text.isEmpty())
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_LONG).show()
        else {
            saveData()
        }
    }

    private fun saveData() {
        if (tietMonthlyBudget.text.isNotEmpty())
            monthlyBudget = tietMonthlyBudget.text.toString().toFloat()

        if (tietMonthlyIncome.text.isNotEmpty())
            monthlyIncome = tietMonthlyIncome.text.toString().toFloat()

        userName = tietUserName.text.toString().trim()

        val sharedPreferences =
            requireActivity().getSharedPreferences("EXPENSE_MANAGER", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString("USER_NAME", userName)
            putFloat("MONTHLY_BUDGET", monthlyBudget)
            putFloat("MONTHLY_INCOME", monthlyIncome)
            putBoolean("PREVIOUSLY_STARTED", true)
            commit()
        }

        findNavController().navigate(R.id.action_onboarding_to_tab)
    }
}