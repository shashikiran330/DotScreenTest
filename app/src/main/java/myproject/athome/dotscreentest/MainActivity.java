package myproject.athome.dotscreentest;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public int counter;
    LayoutInflater inflater;

    private final Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;

    private int roundCount;

    private int player1Score;
    private int player2Score;

    private TextView textPlayer1;
    private TextView textPlayer2;

    private ImageView arrow1;
    private ImageView arrow2;


    private TextView winnerName;
    private Button btn_reStart;
    private Button btn_exit;

    private AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////////////////////////////////////////////////////////////////////////////////////
        textPlayer1 = findViewById(R.id.ps1);
        textPlayer2 = findViewById(R.id.ps2);
        arrow1 = findViewById(R.id.pa1);
        arrow2 = findViewById(R.id.pa2);

        arrow2.setVisibility(View.GONE);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(MainActivity.this);
            }
        }

        gameTimer();


    }

    private void gameTimer() {

        final  TextView  countTime = findViewById(R.id.timer);
        new CountDownTimer(180000, 1000){
            public void onTick(long millisUntilFinished){

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                countTime.setText(f.format(min) + ":" + f.format(sec));
                counter++;
            }
            public  void onFinish(){
                countTime.setText("00:00");

                alert = new AlertDialog.Builder(MainActivity.this);
                View mView = inflater.inflate(R.layout.game_end_alert, null, false);

                btn_reStart = mView.findViewById(R.id.btn_reStart);
                btn_exit = mView.findViewById(R.id.btn_exit);
                winnerName = mView.findViewById(R.id.winner_name);

                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);

                if(player1Score > player2Score){
                    winnerName.setText("Winner: Player 1 ("  +player1Score+"/"+player2Score +")!");
                } else if(player2Score > player1Score){
                    winnerName.setText("Winner: Player 2 ("  +player2Score+"/"+player1Score +")!");
                } else {
                    winnerName.setText("Game Draws !!!");
                }

                btn_reStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                alertDialog.show();

            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText("X");
            arrow1.setVisibility(View.GONE);
            arrow2.setVisibility(View.VISIBLE);

        } else {
            ((Button) v).setText("O");
            arrow1.setVisibility(View.VISIBLE);
            arrow2.setVisibility(View.GONE);

        }
        roundCount++;
        if (checkingForWinner()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkingForWinner() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
    private void player1Wins() {
        player1Score++;
        updateWinnerPoints();
        resetBoard();
    }
    private void player2Wins() {
        player2Score++;
        updateWinnerPoints();
        resetBoard();
    }
    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updateWinnerPoints() {
        textPlayer1.setText( ""+ player1Score);
        textPlayer2.setText( ""+ player2Score);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
        arrow1.setVisibility(View.VISIBLE);
        arrow2.setVisibility(View.GONE);
    }

}