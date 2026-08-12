import streamlit as st
import requests


# Config

API_BASE = "http://localhost:8080/api"

st.set_page_config(page_title="LinkNet", page_icon="🔗", layout="wide")


st.markdown("""
<style>
@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=Inter:wght@400;500;600&display=swap');

html, body, [class*="css"] {
    font-family: 'Inter', sans-serif;
    font-size: 17px;
}

h1, h2, h3 {
    font-family: 'Space Grotesk', sans-serif !important;
    letter-spacing: -0.01em;
}

.block-container {
    max-width: 900px;
    padding-top: 2rem;
}

/* Wordmark: two overlapping color-blocked rings for the "Link" of
   LinkNet, sized to actually read as a logo, not a tiny favicon */
.wordmark {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1.5rem;
}
.wordmark .rings {
    position: relative;
    width: 52px;
    height: 52px;
    flex-shrink: 0;
}
.wordmark .rings span {
    position: absolute;
    width: 32px;
    height: 32px;
    border: 5px solid #FF5D5D;
    border-radius: 50%;
}
.wordmark .rings span:first-child { top: 0; left: 0; }
.wordmark .rings span:last-child { bottom: 0; right: 0; border-color: #14B8A6; }
.wordmark h1 {
    font-size: 2.75rem !important;
    margin: 0 !important;
    color: #241B3A;
}

/* Tabs: bigger labels, bigger icons, clear active-state color */
.stTabs [data-baseweb="tab-list"] {
    gap: 1.5rem;
}
.stTabs [data-baseweb="tab"] {
    font-size: 1.05rem;
    font-weight: 600;
    padding: 0.75rem 0.25rem;
}
.stTabs [aria-selected="true"] {
    color: #FF5D5D !important;
}

/* Card containers (st.container(border=True)) */
div[data-testid="stVerticalBlockBorderWrapper"] {
    border-radius: 16px !important;
    border: 2px solid #F0E6FF !important;
    background-color: #FFFFFF;
    box-shadow: 0 2px 10px rgba(36, 27, 58, 0.05);
}

/* Buttons: solid coral, teal for secondary/outline-style actions */
.stButton button, .stFormSubmitButton button {
    border-radius: 10px;
    font-weight: 600;
    font-size: 1rem;
}
.stFormSubmitButton button {
    background-color: #FF5D5D;
    color: white;
    border: none;
}
.stFormSubmitButton button:hover {
    background-color: #E64545;
    color: white;
}

/* Section headers get a small teal accent bar for visual rhythm */
h3 {
    border-left: 5px solid #14B8A6;
    padding-left: 0.6rem;
}
</style>
""", unsafe_allow_html=True)


def api_get(path, **kwargs):
    try:
        return requests.get(f"{API_BASE}{path}", **kwargs)
    except requests.exceptions.ConnectionError:
        st.error("Could not reach the backend. Is Spring Boot running on localhost:8080?")
        return None


def api_post(path, **kwargs):
    try:
        return requests.post(f"{API_BASE}{path}", **kwargs)
    except requests.exceptions.ConnectionError:
        st.error("Could not reach the backend. Is Spring Boot running on localhost:8080?")
        return None


def api_delete(path, **kwargs):
    try:
        return requests.delete(f"{API_BASE}{path}", **kwargs)
    except requests.exceptions.ConnectionError:
        st.error("Could not reach the backend. Is Spring Boot running on localhost:8080?")
        return None


def api_put(path, **kwargs):
    try:
        return requests.put(f"{API_BASE}{path}", **kwargs)
    except requests.exceptions.ConnectionError:
        st.error("Could not reach the backend. Is Spring Boot running on localhost:8080?")
        return None


def error_message(response):
    """Backend now returns {"error": "..."} for IllegalStateException cases
    (via GlobalExceptionHandler), but falls back to raw text for anything else
    (e.g. uncaught DataIntegrityViolationException, still a plain 500)."""
    try:
        body = response.json()
        return body.get("error", response.text)
    except ValueError:
        return response.text


def get_username(user_id):
    """Friendship rows only store raw user ids (no @ManyToOne to User),
    so the frontend has to look each one up separately to show a name
    instead of a bare number."""
    response = api_get(f"/users/{user_id}")
    if response is not None and response.status_code == 200:
        return response.json().get("username", f"User {user_id}")
    return f"User {user_id}"



# Session: "logged in as" 

if "current_user" not in st.session_state:
    st.session_state.current_user = None

st.markdown(
    """<div class="wordmark"><div class="rings"><span></span><span></span></div><h1>LinkNet</h1></div>""",
    unsafe_allow_html=True,
)

with st.sidebar:
    st.subheader("Session")
    if st.session_state.current_user:
        st.success(f"Logged in as **{st.session_state.current_user['username']}** (id {st.session_state.current_user['id']})")
        if st.button("Log out"):
            st.session_state.current_user = None
            st.rerun()
    else:
        st.info("Not logged in. Use the Sign Up / Log In tab.")

tab_auth, tab_feed, tab_friends, tab_profile = st.tabs(
    ["👤  Sign Up / Log In", "📰  Feed", "🤝  Friends", "🧑  Profile"]
)


#  Sign Up / Log In 

with tab_auth:
    col_signup, col_login = st.columns(2, gap="large")

    with col_signup:
        with st.container(border=True):
            st.subheader("Create account")
            with st.form("signup_form", clear_on_submit=True):
                username = st.text_input("Username")
                email = st.text_input("Email")
                full_name = st.text_input("Full name")
                password = st.text_input("Password", type="password")
                bio = st.text_area("Bio", height=80)
                signup_submitted = st.form_submit_button("Sign up")

                if signup_submitted:
                    if not username or not email or not password:
                        st.warning("Username, email, and password are required.")
                    else:
                        response = api_post(
                            "/users",
                            json={
                                "username": username,
                                "email": email,
                                "fullName": full_name,
                                "password": password,
                                "bio": bio,
                            },
                        )
                        if response is not None:
                            if response.status_code == 200:
                                new_user = response.json()
                                st.success(f"Account created! Your user id is {new_user['id']}.")
                            else:
                                st.error(error_message(response))

    with col_login:
        with st.container(border=True):
            st.subheader("Log in")
            # No real login/password check yet
            st.caption("No password check yet — just pick your user id for this session.")
            login_id = st.number_input("Your user ID", min_value=1, step=1, key="login_id")
            if st.button("Log in"):
                response = api_get(f"/users/{int(login_id)}")
                if response is not None:
                    if response.status_code == 200:
                        st.session_state.current_user = response.json()
                        st.rerun()
                    else:
                        st.error(error_message(response))


#  Feed

with tab_feed:
    st.subheader("Create a post")

    if not st.session_state.current_user:
        st.info("Log in first (see the Sign Up / Log In tab) to create a post.")
    else:
        with st.form("create_post_form", clear_on_submit=True):
            caption = st.text_area("Caption")
            uploaded_file = st.file_uploader(
                "Image or video", type=["jpg", "jpeg", "png", "gif", "mp4", "mov", "webm"]
            )
            submitted = st.form_submit_button("Post")

            if submitted:
                if not caption or not uploaded_file:
                    st.warning("Caption and a file are both required.")
                else:
                    files = {
                        "file": (uploaded_file.name, uploaded_file.getvalue(), uploaded_file.type)
                    }
                    upload_response = api_post("/posts/upload", files=files)

                    if upload_response is not None:
                        if upload_response.status_code != 200:
                            st.error(f"Upload failed: {error_message(upload_response)}")
                        else:
                            media_url = upload_response.text.strip('"')
                            response = api_post(
                                "/posts",
                                params={
                                    "userId": st.session_state.current_user["id"],
                                    "caption": caption,
                                    "data": media_url,
                                },
                            )
                            if response is not None:
                                if response.status_code == 200:
                                    st.success("Post created!")
                                else:
                                    st.error(error_message(response))

    st.divider()
    st.subheader("Feed")

    if st.button("Refresh feed"):
        st.rerun()

    response = api_get("/posts")
    if response is not None and response.status_code == 200:
        posts = response.json()

        if not posts:
            st.info("No posts yet — be the first!")

        for post in reversed(posts):
            with st.container(border=True):
                author = post.get("user") or {}
                st.markdown(f"**{author.get('username', 'Unknown user')}**")
                st.write(post.get("caption", ""))

                if post.get("video"):
                    st.video(post["video"])
                elif post.get("image"):
                    st.image(post["image"])

                st.caption(post.get("createdAt", ""))

                # --- Comments ---
                post_id = post.get("id")
                comments_response = api_get(f"/comments/post/{post_id}")
                comments = comments_response.json() if comments_response is not None and comments_response.status_code == 200 else []

                with st.expander(f"💬 {len(comments)} comment(s)"):
                    for c in comments:
                        c_author = (c.get("user") or {}).get("username", "Unknown user")
                        st.markdown(f"**{c_author}**: {c.get('content', '')}")

                    if st.session_state.current_user:
                        comment_key = f"comment_input_{post_id}"
                        new_comment = st.text_input("Add a comment", key=comment_key)
                        if st.button("Send", key=f"comment_btn_{post_id}"):
                            if new_comment.strip():
                                c_response = api_post(
                                    "/comments",
                                    params={
                                        "userId": st.session_state.current_user["id"],
                                        "postId": post_id,
                                    },
                                    json={"content": new_comment},
                                )
                                if c_response is not None:
                                    if c_response.status_code == 200:
                                        st.rerun()
                                    else:
                                        st.error(error_message(c_response))
                    else:
                        st.caption("Log in to comment.")
    elif response is not None:
        st.error(f"Failed to load feed ({response.status_code})")


# Friends

with tab_friends:
    if not st.session_state.current_user:
        st.info("Log in first (see the Sign Up / Log In tab) to manage friends.")
    else:
        my_id = st.session_state.current_user["id"]

        st.subheader("Add a friend")
        with st.form("add_friend_form", clear_on_submit=True):
            friend_id = st.number_input("Friend's user ID", min_value=1, step=1)
            add_submitted = st.form_submit_button("Send friend request")

            if add_submitted:
                response = api_post(
                    "/friendships",
                    params={"userId1": my_id, "userId2": int(friend_id)},
                )
                if response is not None:
                    if response.status_code == 200:
                        st.success("Friendship created!")
                    else:
                        st.error(error_message(response))

        st.divider()
        st.subheader("Your friends")

        response = api_get("/friendships")
        if response is not None and response.status_code == 200:
            all_friendships = response.json()
            my_friendships = [
                f for f in all_friendships
                if f.get("userId1") == my_id or f.get("userId2") == my_id
            ]

            if not my_friendships:
                st.info("No friends yet.")
            else:
                for f in my_friendships:
                    other_id = f["userId2"] if f["userId1"] == my_id else f["userId1"]
                    col1, col2 = st.columns([4, 1])
                    with col1:
                        st.write(f"**{get_username(other_id)}**  \n_id {other_id}_")
                    with col2:
                        if st.button("Remove", key=f"remove_{f['friendshipId']}"):
                            del_response = api_delete(f"/friendships/{f['friendshipId']}")
                            if del_response is not None and del_response.status_code == 200:
                                st.rerun()

        st.divider()
        st.subheader("People you may know")

        response = api_get(f"/network/recommendations/{my_id}")
        if response is not None:
            if response.status_code == 204:
                st.info("No recommendations yet — add a few friends first.")
            elif response.status_code == 200:
                recommended_ids = response.json()
                for rid in recommended_ids:
                    col1, col2 = st.columns([4, 1])
                    with col1:
                        st.write(f"**{get_username(rid)}**  \n_id {rid}_")
                    with col2:
                        if st.button("Add", key=f"rec_add_{rid}"):
                            add_response = api_post(
                                "/friendships",
                                params={"userId1": my_id, "userId2": rid},
                            )
                            if add_response is not None:
                                if add_response.status_code == 200:
                                    st.rerun()
                                else:
                                    st.error(error_message(add_response))


#  Profile

with tab_profile:
    if not st.session_state.current_user:
        st.info("Log in first (see the Sign Up / Log In tab) to view your profile.")
    else:
        my_id = st.session_state.current_user["id"]

        
        response = api_get(f"/users/{my_id}")
        if response is not None and response.status_code == 200:
            me = response.json()

            st.subheader(f"@{me.get('username', '')}")
            if me.get("fullName"):
                st.write(me["fullName"])
            if me.get("bio"):
                st.caption(me["bio"])
            st.write(f"📧 {me.get('email', '')}")

            with st.expander("Edit profile"):
                with st.form("edit_profile_form"):
                    edit_full_name = st.text_input("Full name", value=me.get("fullName", ""))
                    edit_bio = st.text_area("Bio", value=me.get("bio", ""), height=80)
                    edit_password = st.text_input(
                        "New password", type="password",
                        help="Leave blank to keep your current password.",
                    )
                    
                    st.caption("Username and email can't be changed here.")
                    profile_submitted = st.form_submit_button("Save changes")

                    if profile_submitted:
                        payload = {"fullName": edit_full_name, "bio": edit_bio}
                        if edit_password:
                            payload["password"] = edit_password
                        edit_response = api_put(f"/users/{my_id}", json=payload)
                        if edit_response is not None:
                            if edit_response.status_code == 200:
                                updated = edit_response.json()
                                st.session_state.current_user = updated
                                st.success("Profile updated!")
                                st.rerun()
                            else:
                                st.error(error_message(edit_response))

            st.divider()
            st.subheader("Your friends")
            friendships_response = api_get("/friendships")
            if friendships_response is not None and friendships_response.status_code == 200:
                all_friendships = friendships_response.json()
                my_friendships = [
                    f for f in all_friendships
                    if f.get("userId1") == my_id or f.get("userId2") == my_id
                ]
                if not my_friendships:
                    st.caption("No friends yet — head to the Friends tab to add some.")
                else:
                    friend_names = [
                        get_username(f["userId2"] if f["userId1"] == my_id else f["userId1"])
                        for f in my_friendships
                    ]
                    st.write(f"**{len(friend_names)}** friend(s): " + ", ".join(friend_names))
        else:
            st.error("Could not load profile.")

        st.divider()
        st.subheader("Your posts")

        posts_response = api_get("/posts")
        if posts_response is not None and posts_response.status_code == 200:
            all_posts = posts_response.json()
            
            my_posts = [p for p in all_posts if (p.get("user") or {}).get("id") == my_id]

            if not my_posts:
                st.info("You haven't posted anything yet.")
            else:
                # Tracks which single post (by id) is currently in edit mode,
                # so only one edit form is open at a time.
                if "editing_post_id" not in st.session_state:
                    st.session_state.editing_post_id = None

                for post in reversed(my_posts):
                    post_id = post.get("id")
                    with st.container(border=True):
                        if st.session_state.editing_post_id == post_id:
                            new_caption = st.text_area(
                                "Edit caption",
                                value=post.get("caption", ""),
                                key=f"edit_caption_{post_id}",
                            )
                            col_save, col_cancel = st.columns(2)
                            with col_save:
                                if st.button("Save", key=f"save_{post_id}"):
                                   
                                    save_response = api_put(
                                        f"/posts/{post_id}",
                                        params={"caption": new_caption},
                                    )
                                    if save_response is not None:
                                        if save_response.status_code == 200:
                                            st.session_state.editing_post_id = None
                                            st.rerun()
                                        else:
                                            st.error(error_message(save_response))
                            with col_cancel:
                                if st.button("Cancel", key=f"cancel_{post_id}"):
                                    st.session_state.editing_post_id = None
                                    st.rerun()
                        else:
                            st.write(post.get("caption", ""))

                        if post.get("video"):
                            st.video(post["video"])
                        elif post.get("image"):
                            st.image(post["image"])
                        st.caption(post.get("createdAt", ""))

                        if st.session_state.editing_post_id != post_id:
                            col_edit, col_delete = st.columns(2)
                            with col_edit:
                                if st.button("Edit", key=f"edit_btn_{post_id}"):
                                    st.session_state.editing_post_id = post_id
                                    st.rerun()
                            with col_delete:
                                confirm_key = f"confirm_delete_{post_id}"
                                if st.session_state.get(confirm_key, False):
                                    if st.button("Confirm delete?", key=f"confirm_btn_{post_id}"):
                                       
                                        del_response = api_delete(f"/posts/{post_id}")
                                        if del_response is not None:
                                            if del_response.status_code == 200:
                                                st.session_state[confirm_key] = False
                                                st.rerun()
                                            else:
                                                st.error(error_message(del_response))
                                else:
                                    if st.button("Delete", key=f"delete_btn_{post_id}"):
                                        st.session_state[confirm_key] = True
                                        st.rerun()
        else:
            st.error("Could not load your posts.")