package com.example.baitaplaydiem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView pointTView;
    private TextView questionTView;
    private EditText answerEditText;
    private Button btnConfirm;
    private List<Question> questionList;
    private Question currentQuestionObj;
    private int currentQuestionIndex = 0;
    private int points = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initUI();
        sharedPreferences = getSharedPreferences("point", MODE_PRIVATE);

        // Khởi tạo danh sách câu hỏi
        questionList = getListOfQuestions();
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào!", Toast.LENGTH_SHORT).show();
            return;
        }
        setQuestionData(questionList.get(currentQuestionIndex));

        // Thiết lập sự kiện cho nút xác nhận
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });

        // Khởi tạo điểm từ SharedPreferences nếu có
        points = sharedPreferences.getInt("point", 0);
        updatePointDisplay();
    }

    private void initUI() {
        pointTView = findViewById(R.id.point);
        questionTView = findViewById(R.id.question);
        answerEditText = findViewById(R.id.answer);
        btnConfirm = findViewById(R.id.confirm_button);
    }

    private void checkAnswer() {
        String answerString = answerEditText.getText().toString().trim();
        if (answerString.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập câu trả lời", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int answer = Integer.parseInt(answerString);
            if (answer == currentQuestionObj.getResult()) {
                Toast.makeText(MainActivity.this, "Đáp án đúng", Toast.LENGTH_SHORT).show();
                points += 10;
            } else {
                Toast.makeText(MainActivity.this, "Đáp án sai", Toast.LENGTH_SHORT).show();
            }
            updatePoints();
            moveToNextQuestion();
            answerEditText.setText("");
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            setQuestionData(questionList.get(currentQuestionIndex));
        } else {
            Toast.makeText(MainActivity.this, "Bạn đã hoàn thành tất cả câu hỏi!", Toast.LENGTH_SHORT).show();
            currentQuestionIndex = 0;
            setQuestionData(questionList.get(currentQuestionIndex));
        }
    }

    private void updatePoints() {
        // Cập nhật điểm và lưu trữ vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("point", points);
        editor.apply();
        updatePointDisplay();
    }

    private void updatePointDisplay() {
        pointTView.setText("Point: " + points);
    }

    private void setQuestionData(Question question) {
        if (question == null) return;
        currentQuestionObj = question;
        questionTView.setText(question.getQuestion());
    }

    private List<Question> getListOfQuestions() {
        // Tạo danh sách các câu hỏi
        List<Question> list = new ArrayList<>();
        list.add(new Question("4 + 5 =", 9));
        list.add(new Question("4 + 9 =", 13));
        list.add(new Question("1 + 1 =", 2));
        list.add(new Question("6 + 7 =", 13));
        list.add(new Question("8 + 3 =", 11));
        return list;
    }
}
