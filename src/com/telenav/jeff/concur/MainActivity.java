package com.telenav.jeff.concur;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.telenav.jeff.concur.service.ExpenseService;
import com.telenav.jeff.concur.service.TokenService;
import com.telenav.jeff.concur.service.UserService;
import com.telenav.jeff.concur.vo.Token;

public class MainActivity extends Activity
{
    
    private TextView tokenTextView;
    private Button userBtn;
    private Button expenseListBtn;
    
    private SimpleAsyncTask simpleAsyncTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tokenTextView = (TextView)findViewById(R.id.tokenTextView);
        userBtn = (Button)findViewById(R.id.user);
        expenseListBtn = (Button)findViewById(R.id.expense_list);
        ButtonClickListener listener = new ButtonClickListener();
        userBtn.setOnClickListener(listener );
        expenseListBtn.setOnClickListener(listener);
        
        boolean isLogin = getIntent().getExtras().getBoolean("isLogin");
        if (isLogin)
        {
            tokenTextView.setText("Loing is OK");
        }
        else
        {
            final String code = getIntent().getExtras().getString("code");
            simpleAsyncTask = new SimpleAsyncTask();
            simpleAsyncTask.setProgressText("Authenticating");
            simpleAsyncTask.execute(new Command(){

                @Override
                public Void execute()
                {
                    new TokenService().requestToken(code);
                    return null;
                }
 
            });
        }
    }
    
    private class SimpleAsyncTask extends AsyncTask<Command, Void, Void>
    {
        private ProgressDialog progress;
        
        public SimpleAsyncTask()
        {
            progress = new ProgressDialog(MainActivity.this);
            progress.setTitle("");
        }
        
        public void setProgressText(String message)
        {
            progress.setMessage(message + "...");
        }
        
        @Override
        protected void onPreExecute()
        {
            progress.show();
            super.onPreExecute();
        }
        
        @Override
        protected Void doInBackground(Command... params)
        {
            return params[0].execute();
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if (progress != null)
            {
                progress.dismiss();
            }
        }
        
    }
    
    private class ButtonClickListener implements OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            simpleAsyncTask = new SimpleAsyncTask();
            simpleAsyncTask.setProgressText("Please wait");
            simpleAsyncTask.execute(getCommand(id));
        }

        
    }
    
    private Command getCommand(int id)
    {
        Command command = null;
        
        switch (id)
        {
            case R.id.expense_list:
                command = new Command()
                {
                    @Override
                    public Void execute()
                    {
                       new ExpenseService().getExpenseReports();
                       return null;
                    }
                };
                break;
            case R.id.user:
                command = new Command()
                {
                    @Override
                    public Void execute()
                    {
                        new UserService().getUser();
                        return null;
                    }
                };
                break;
            default:
                break;
        }
        
        return command;
        
    }
    
    private interface Command
    {
        Void execute();
    }
}
