package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import alert.alert;
import alert.check;
import connection.chatQuery;
import connection.friendQuery;
import connection.userQuery;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.chats;
import models.friends;
import models.users;

public class HomeController extends Thread implements Initializable, Runnable{
	@FXML
	private VBox sidebar;
	@FXML
	private VBox sidebar_chat;
	@FXML
	private VBox sidebar_group;
	@FXML
	private VBox sidebar_friend;
	@FXML
	private VBox sidebar_setting;
	@FXML
	private VBox sidebar_logout;
	@FXML
	private TextField search_content;
	@FXML
	private VBox list_user;
	@FXML
	private TextField send_mes;
	@FXML
	private VBox chat_pane;
	@FXML
	private HBox user_chat_infor;
	@FXML
	private ImageView send_btn;
	@FXML
	private VBox load_db;
	@FXML
	private ImageView avatar_id;
	
	private Socket s;
	private BufferedReader bfr;
	private BufferedWriter bfw;
	
	friendQuery friendq = new friendQuery();
	chatQuery chatq = new chatQuery();
	
	public void createSocket() {
		try {
			this.s = new Socket("127.0.0.1", 4444);
			this.bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.bfw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}
		catch (IOException e) {
			System.out.print("Not Create Socket!");
			closeEverything(this.s, bfr, bfw);
		}
	}
	
	public void closeEverything(Socket s, BufferedReader bfr, BufferedWriter bfw) {
		try {
			if (bfr != null) {
				bfr.close();
			}
			if (bfw != null) {
				bfw.close();
			}
			if (s != null) {
				s.close();
			}
		}
		catch(IOException e) {
			
		}
	}
	
	public void recv() {
		try {
			String mes = bfr.readLine();
			System.out.print(mes);
		}
		catch(IOException e) {
			closeEverything(s, bfr, bfw);
		}
	}
	
	public void send(String mes) {
		try {
			this.bfw.write(mes);
			this.bfw.flush();
		}
		catch(IOException e) {
			closeEverything(s, bfr, bfw);
		}
	}
	
	// variable
	users user = null;
	users currentChat = null;
	userQuery userq = new userQuery();
	public int countChat = 0;
	
	// load list friends
	List<users> list;
	List<friends> listMF;
	List<EventHandler<MouseEvent>> listEvent = new ArrayList<EventHandler<MouseEvent>>();
	
	// component friends
	public HBox HboxFriends(users user, int item) {
		HBox box = new HBox();
		ImageView img = new ImageView();
		img.setImage(new Image(user.getAvatar()));
		img.setFitWidth(60);
		img.setFitHeight(60);
		img.getStyleClass().add("child--avatar");
		Label name = new Label();
		name.setText("   " + user.getUsername());
		box.setPadding(new Insets(10, 20, 10, 20));
		box.setAlignment(Pos.CENTER_LEFT);
		box.getStyleClass().add("hbox");
		box.getChildren().add(img);
		box.getChildren().add(name);
		box.setOnMouseClicked(listEvent.get(item));
		return box;
	}
	
	public void setFriend(users user) {
		ImageView img = new ImageView();
		img.setImage(new Image(user.getAvatar()));
		img.setFitWidth(60);
		img.setFitHeight(60);
		img.getStyleClass().add("child--avatar");
		Label name = new Label();
		name.setText("   " + user.getUsername());
		user_chat_infor.setPadding(new Insets(10, 20, 10, 20));
		user_chat_infor.setAlignment(Pos.CENTER_LEFT);
		user_chat_infor.getStyleClass().add("hbox");
	    user_chat_infor.getChildren().add(img);
		user_chat_infor.getChildren().add(name);
	}
	
	public HBox setMess(String text) {
		HBox div_mes = new HBox();
		div_mes.setPadding(new Insets(10));
		div_mes.setAlignment(Pos.CENTER_RIGHT);
		Label mes = new Label();
		mes.setStyle("-fx-text-fill:WHITE; "
				+ "-fx-background-color: rgba(254, 44, 85, 1.0); "
				+ "-fx-padding: 10px; -fx-background-radius: 5px;");
		mes.setText(text);
		div_mes.getChildren().add(mes);
		div_mes.setLayoutX(530);
		return div_mes;
	}
	
	public HBox setRecv(String text) {
		HBox div_mes = new HBox();
		div_mes.setPadding(new Insets(10));
		div_mes.setAlignment(Pos.CENTER_LEFT);
		Label mes = new Label();
		mes.setStyle("-fx-text-fill:black; "
				+ "-fx-background-color: rgba(0, 0, 0, 0.07); "
				+ "-fx-padding: 10px; -fx-background-radius: 5px;");
		mes.setText(text + "  ");
		div_mes.getChildren().add(mes);
		div_mes.setLayoutX(30);
		return div_mes;
	}
	
	public VBox setSendFile(String path, String text) {
		VBox div_file = new VBox();
		div_file.setPadding(new Insets(10));
		div_file.setAlignment(Pos.CENTER_RIGHT);
		ImageView img = new ImageView();
		img.setImage(new Image("src\\assets\\image\\file.png"));
		img.setFitHeight(50);
		img.setFitWidth(50);
		div_file.getChildren().add(img);
		Label mes = new Label();
		mes.setText(text);
		div_file.getChildren().add(mes);
		div_file.setLayoutX(530);
		div_file.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				File file = new File(path);
				try {
					try (FileInputStream fis = new FileInputStream(file)) {
						String s = "";
						int r = 0;
						while ((r = fis.read()) != -1) {
							s = s + (char) r;
						}
						File dir = new DirectoryChooser().showDialog(null);
						if (dir != null) {
							File nFile = new File(dir.getAbsolutePath() + "\\" + text);
							try {
								if (nFile.createNewFile()) {
									// success
								}
								else {
									new alert().alertFieldError("File is existed!");
								}
							}
							catch (IOException e) {
								e.printStackTrace();
							}
							
							FileWriter newFile = new FileWriter(dir.getAbsolutePath() + "\\" + text);
							newFile.write(s);
							newFile.close();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return div_file;
	}
	
	public VBox setRecvFile(String path, String text) {
		VBox div_file = new VBox();
		div_file.setPadding(new Insets(10));
		div_file.setAlignment(Pos.CENTER_LEFT);
		ImageView img = new ImageView();
		img.setImage(new Image("src\\assets\\image\\file1.png"));
		img.setFitHeight(50);
		img.setFitWidth(50);
		div_file.getChildren().add(img);
		Label mes = new Label();
		mes.setText(text);
		div_file.getChildren().add(mes);
		div_file.setLayoutX(30);
		div_file.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				File file = new File(path);
				try {
					try (FileInputStream fis = new FileInputStream(file)) {
						String s = "";
						int r = 0;
						while ((r = fis.read()) != -1) {
							s = s + (char) r;
						}
						File dir = new DirectoryChooser().showDialog(null);
						if (dir != null) {
							File nFile = new File(dir.getAbsolutePath() + "\\" + text);
							try {
								if (nFile.createNewFile()) {
									// success
								}
								else {
									new alert().alertFieldError("File is existed!");
								}
							}
							catch (IOException e) {
								e.printStackTrace();
							}
							
							FileWriter newFile = new FileWriter(dir.getAbsolutePath() + "\\" + text);
							newFile.write(s);
							newFile.close();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return div_file;
	}
	
	public VBox setSendImg(String path, String text) {
		VBox div_file = new VBox();
		div_file.setPadding(new Insets(10));
		div_file.setAlignment(Pos.CENTER_RIGHT);
		ImageView img = new ImageView();
		img.setImage(new Image(path));
		img.setFitHeight(200);
		img.setFitWidth(200);
		div_file.getChildren().add(img);
		div_file.setLayoutX(530);
		img.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				File file = new File(path);
				try {
					BufferedImage image = ImageIO.read(file);
					try {
						File dir = new DirectoryChooser().showDialog(null);
						if (dir != null) {
							File nFile = new File(dir.getAbsolutePath() + "\\" + text);
							if (nFile.createNewFile()) {
								File newFile = new File(dir.getAbsolutePath() + "\\" + text);
								ImageIO.write(image, "PNG", newFile);
							}
							else {
								new alert().alertFieldError("File is existed!");
							}
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return div_file;
	}
	
	public VBox setRecvImg(String path, String text) {
		VBox div_file = new VBox();
		div_file.setPadding(new Insets(10));
		div_file.setAlignment(Pos.CENTER_LEFT);
		ImageView img = new ImageView();
		img.setImage(new Image(path));
		img.setFitHeight(200);
		img.setFitWidth(200);
		div_file.getChildren().add(img);
		div_file.setLayoutX(30);
		img.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				File file = new File(path);
				try {
					BufferedImage image = ImageIO.read(file);
					File dir = new DirectoryChooser().showDialog(null);
					try {
						if (dir != null) {
							File nFile = new File(dir.getAbsolutePath() + "\\" + text);
							if (nFile.createNewFile()) {
								File newFile = new File(dir.getAbsolutePath() + "\\" + text);
								ImageIO.write(image, "PNG", newFile);
							}
							else {
								new alert().alertFieldError("File is existed!");
							}
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return div_file;
	}
	
	public void loadChatFromVbox(List<chats> listChat) {
		if (listChat != null) {
			for (int i = 0; i < listChat.size(); i++) {
				chats chat = listChat.get(i);
				if (chat.getType() == 1) {
					if (chat.getU1() == user.getId() && chat.getU2() == currentChat.getId()) {
						chat_pane.getChildren().add(setMess(chat.getContent()));
					}
	                if (chat.getU2() == user.getId() && chat.getU1() == currentChat.getId()) {
	                	chat_pane.getChildren().add(setRecv(chat.getContent()));
					}
				}
				if (chat.getType() == 2) {
					if (chat.getU1() == user.getId() && chat.getU2() == currentChat.getId()) {
						chat_pane.getChildren().add(setSendFile(chat.getContent(), chat.getName()));
					}
	                if (chat.getU2() == user.getId() && chat.getU1() == currentChat.getId()) {
	                	chat_pane.getChildren().add(setRecvFile(chat.getContent(), chat.getName()));
					}
				}
				if (chat.getType() == 3) {
					if (chat.getU1() == user.getId() && chat.getU2() == currentChat.getId()) {
						chat_pane.getChildren().add(setSendImg(chat.getContent(), chat.getName()));
					}
	                if (chat.getU2() == user.getId() && chat.getU1() == currentChat.getId()) {
	                	chat_pane.getChildren().add(setRecvImg(chat.getContent(), chat.getName()));
					}
				}
			}
		}
	}

			
	public void setUser(users u) throws SQLException, IOException {
		user = u;
		avatar_id.setImage(new Image(u.getAvatar()));
		avatar_id.getStyleClass().add("avatar--child");
		avatar_id.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				new alert().alertSuccess(u.getUsername());
			}
			
		});
		send(u.getUsername());
		list = userq.loadAllUser();
		listMF = friendq.listMF();
		check ck = new check(listMF, null);
		for (int i = 0; i < list.size(); i++) {
			users ur = list.get(i);
			if (ck.checkInMF(user.getId(), ur.getId()) == 0 && ur.getId() != user.getId()) {
				EventHandler<MouseEvent> handlerChangeUser = event -> {
				    if (ur != currentChat) {
				    	chat_pane.getChildren().clear();
					    user_chat_infor.getChildren().clear();
					    setFriend(ur);
					    currentChat = ur;
					    try {
							List<chats> listChat = chatq.loadChat();
							loadChatFromVbox(listChat);
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    }
				};
				listEvent.add(handlerChangeUser);
			}
		}
		this.currentChat = null;
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			users ur = list.get(i);
			if (ck.checkInMF(user.getId(), ur.getId()) == 0 && ur.getId() != user.getId()) {
			    list_user.getChildren().add(HboxFriends(list.get(i), count));
			    count ++;
			}
		}
	}
	

	

	// Event Listener on VBox[#sidebar_chat].onMouseClicked
	@FXML
	public void onClickMess(MouseEvent event) {
		
	}
	// Event Listener on ImageView.onMouseEntered
	@FXML
	public void onER1(MouseEvent event) {
		
	}
	// Event Listener on ImageView.onMouseMoved
	@FXML
	public void onMV1(MouseEvent event) {
		
	}
	// Event Listener on VBox[#sidebar_group].onMouseClicked
	@FXML
	public void onClickGroupChat(MouseEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("All.fxml"));
		Parent root = loader.load();
		AllController all = loader.getController();
		all.setUser(user);
		Stage window = (Stage) sidebar_group.getScene().getWindow();
		window.setScene(new Scene(root));
	}
	// Event Listener on VBox[#sidebar_friend].onMouseClicked
	@FXML
	public void onClickFriends(MouseEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Friend.fxml"));
		Parent root = loader.load();
		FriendController friend = loader.getController();
		friend.setUser(user);
		friend.loadNewFriends();
		Stage window = (Stage) sidebar_friend.getScene().getWindow();
		window.setScene(new Scene(root));
	}
	// Event Listener on VBox[#sidebar_setting].onMouseClicked
	@FXML
	public void onClickSetting(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PersonalInfor.fxml"));
		Parent root = loader.load();
		PersonalInforController personal = loader.getController();
		personal.setUser(user);
		Stage window = (Stage) sidebar_setting.getScene().getWindow();
		window.setScene(new Scene(root));
	}
	// Event Listener on VBox[#sidebar_logout].onMouseClicked
	@FXML
	public void onClickLogout(MouseEvent event) throws IOException {
		Stage window = (Stage) sidebar_logout.getScene().getWindow();
		new alert().alertLogout(window);
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void onClickSearch(MouseEvent event) throws SQLException {
		if (search_content.getText() != null && search_content.getText() != "") {
			users u = userq.searchUser(search_content.getText());
			if (u != null) {
				new alert().alertSuccess(u.getUsername() + "-" + u.getEmail() + "-" + u.getPhone());
			}
			else {
				new alert().alertWarning("User was not existed!");
			}
		}
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void onClickImg(MouseEvent event) {
		try {
			File file = new FileChooser().showOpenDialog(null);
			chat_pane.getChildren().add(setSendImg(file.getAbsolutePath(), file.getName()));
			send(file.getAbsolutePath() + "-" + currentChat.getUsername());
			chats chat = new chats(user.getId(), currentChat.getId(), 3, file.getName(), file.getAbsolutePath(), null);
			chatq.addChat(chat);
		}
		catch (Exception e) {
		}
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void onClickFile(MouseEvent event) throws SQLException {
		try {
			File file = new FileChooser().showOpenDialog(null);
			chat_pane.getChildren().add(setSendFile(file.getAbsolutePath(), file.getName()));
			send(file.getAbsolutePath() + "-" + currentChat.getUsername());
			chats chat = new chats(user.getId(), currentChat.getId(), 2, file.getName(), file.getAbsolutePath(), null);
			chatq.addChat(chat);
		}
		catch (Exception e) {
		}
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void onClickVideo(MouseEvent event) {
		
	}
	
	
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void onClickMes(MouseEvent event) throws SQLException {
		if (send_mes.getText() != null && send_mes.getText() != "") {
			chat_pane.getChildren().add(setMess(send_mes.getText()));
			send(send_mes.getText() + "-" + currentChat.getUsername());
			chats chat = new chats(user.getId(), currentChat.getId(), 1, null, send_mes.getText(), null);
			chatq.addChat(chat);
			send_mes.setText(null);
		}
	}
	
	@FXML
	public void onClickLoad(MouseEvent event) throws SQLException {
		if (currentChat != null) {
			chat_pane.getChildren().clear();
			loadChatFromVbox(chatq.loadChat());
		}
	}
	
	
	public static void addLabel(String mes, VBox chat_pane) {
		chat_pane.getChildren().add(new Text(mes));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createSocket();
	}
}
