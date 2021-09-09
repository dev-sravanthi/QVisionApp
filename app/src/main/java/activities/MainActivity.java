package activities;//package activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity {
//    private Button openInputPopupDialogButton = null;
//    // This listview is just under above button.
//    private ListView userDataListView = null;
//    // Below edittext and button are all exist in the popup dialog view.
//    private View popupInputDialogView = null;
//    // Contains user name data.
//    private EditText userNameEditText = null;
//    // Contains password data.
//    private EditText passwordEditText = null;
//    // Contains email data.
//    private EditText emailEditText = null;
//    // Click this button in popup dialog to save user input data in above three edittext.
//    private Button saveUserDataButton = null;
//    // Click this button to cancel edit user data.
//    private Button cancelUserDataButton = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setTitle("dev2qa.com - Android Popup Overlay Input Dialog Example.");
//        initMainActivityControls();
//        // When click the open input popup dialog button.
//        openInputPopupDialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create a AlertDialog Builder.
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//                // Set title, icon, can not cancel properties.
//                alertDialogBuilder.setTitle("User Data Collection Dialog.");
//                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
//                alertDialogBuilder.setCancelable(false);
//                // Init popup dialog view and it's ui controls.
//                initPopupViewControls();
//                // Set the inflated layout view object to the AlertDialog builder.
//                alertDialogBuilder.setView(popupInputDialogView);
//                // Create AlertDialog and show.
//                final AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//                // When user click the save user data button in the popup dialog.
//                saveUserDataButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // Get user data from popup dialog editeext.
//                        String userName = userNameEditText.getText().toString();
//                        String password = passwordEditText.getText().toString();
//                        String email = emailEditText.getText().toString();
//                        // Create data for the listview.
//                        String[] titleArr = {"User Name", "Password", "Email"};
//                        String[] dataArr = {userName, password, email};
//                        ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();
//                        ;
//                        int titleLen = titleArr.length;
//                        for (int i = 0; i < titleLen; i++) {
//                            Map<String, Object> listItemMap = new HashMap<String, Object>();
//                            listItemMap.put("title", titleArr[i]);
//                            listItemMap.put("data", dataArr[i]);
//                            itemDataList.add(listItemMap);
//                        }
//                        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, itemDataList, android.R.layout.simple_list_item_2,
//                                new String[]{"title", "data"}, new int[]{android.R.id.text1, android.R.id.text2});
//                        userDataListView.setAdapter(simpleAdapter);
//                        alertDialog.cancel();
//                    }
//                });
//                cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.cancel();
//                    }
//                });
//            }
//        });
//    }
//
//    /* Initialize main activity ui controls ( button and listview ). */
//    private void initMainActivityControls() {
//        if (openInputPopupDialogButton == null) {
//            openInputPopupDialogButton = (Button) findViewById(R.id.button_popup_overlay_input_dialog);
//        }
//        if (userDataListView == null) {
//            userDataListView = (ListView) findViewById(R.id.listview_user_data);
//        }
//    }
//
//    /* Initialize popup dialog view and ui controls in the popup dialog. */
//    private void initPopupViewControls() {
//        // Get layout inflater object.
//        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
//        // Inflate the popup dialog from a layout xml file.
//        popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_dialog, null);
//        // Get user input edittext and button ui controls in the popup dialog.
//        userNameEditText = (EditText) popupInputDialogView.findViewById(R.id.userName);
//        passwordEditText = (EditText) popupInputDialogView.findViewById(R.id.password);
//        emailEditText = (EditText) popupInputDialogView.findViewById(R.id.email);
//        saveUserDataButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
//        cancelUserDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
//        // Display values from the main activity list view in user input edittext.
//        initEditTextUserDataInPopupDialog();
//    }
//
//    /* Get current user data from listview and set them in the popup dialog edittext controls. */
//    private void initEditTextUserDataInPopupDialog() {
//        List<String> userDataList = getExistUserDataInListView(userDataListView);
//        if (userDataList.size() == 3) {
//            String userName = userDataList.get(0);
//            String password = userDataList.get(1);
//            String email = userDataList.get(2);
//            if (userNameEditText != null) {
//                userNameEditText.setText(userName);
//            }
//            if (passwordEditText != null) {
//                passwordEditText.setText(password);
//            }
//            if (emailEditText != null) {
//                emailEditText.setText(email);
//            }
//        }
//    }
//
//    /* If user data exist in the listview then retrieve them to a string list. */
//    private List<String> getExistUserDataInListView(ListView listView) {
//        List<String> ret = new ArrayList<String>();
//        if (listView != null) {
//            ListAdapter listAdapter = listView.getAdapter();
//            if (listAdapter != null) {
//                int itemCount = listAdapter.getCount();
//                for (int i = 0; i < itemCount; i++) {
//                    Object itemObject = listAdapter.getItem(i);
//                    HashMap<String, String> itemMap = (HashMap<String, String>) itemObject;
//                    Set<String> keySet = itemMap.keySet();
//                    Iterator<String> iterator = keySet.iterator();
//                    String key = iterator.next();
//                    String value = itemMap.get(key);
//                    ret.add(value);
//                }
//            }
//        }
//        return ret;
//    }
//}