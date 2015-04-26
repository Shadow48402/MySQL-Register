<html>
	<body>
		<?php
			/*
			*	Copyright Niels Hamelink
			* 	26-4-2015
			*/
			
			function login($id){
				$_session['id']	=	$id;
				exit('You are logged in');
			}
			
			if(!empty(($_POST['username'])
				&& !empty($_POST['password'])) {
				/*
				*	Config Parts
				*/
				$_config['host']			=	'127.0.0.1';
				$_config['username']		=	'Root';
				$_config['password']		=	'Password';
				$_config['database']		=	'Database';
				$_config['table_name']		=	'users';
				$_config['username_column']	=	'Username';
				$_config['password_column']	=	'Password';
				$_config['hash_name']		=	'MD5';

				// Creatings a (PDO) MySQL connections
				// You can replace it with:
				// $pdo = mysqli_connect(...) if you prefer mysqli above using PDO
				// Don't forget to remove the exception around the connecting
				try {
					$pdo = new PDO ('mysql:host=' . $_config['host'] . ';dbname=' . $_config['database'], $_config['username'], $_config['password']);
				}
				catch (PDOException $ex) {
					die ('Could not connect with the database!');
				}
				
				$query = $pdo->prepare('SELECT id, password FROM :table_name WHERE :username_column=":username_value"');
				$query->execute(
					array(
						'table_name'		=> 	$_config['table_name'],
						'username_column'	=>	$_config['username_column'],
						'username_value'	=>	$_POST['username']
					)
				);
				
				if($query->rowCount() == 1) {
					$result = $query->fetch(PDO::FETCH_ASSOC);
					if(result[$_config['password_column']] == hash($_config['hash_name'], $_POST['password']))
						login($result['id']);
				}
				
				unset($_config, $_POST);
			}
			
		?>
	</body>
</html>