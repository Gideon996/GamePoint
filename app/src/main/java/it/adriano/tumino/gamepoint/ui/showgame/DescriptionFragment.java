package it.adriano.tumino.gamepoint.ui.showgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.adriano.tumino.gamepoint.R;

public class DescriptionFragment extends Fragment {

    private static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sodales est eget interdum sagittis. Duis malesuada nulla odio, sed viverra mauris egestas ut. In feugiat lobortis tortor, ac tincidunt justo lacinia quis. Nullam cursus risus libero, ut cursus enim pulvinar sed. Nam justo risus, viverra sed placerat quis, pretium sit amet elit. Ut accumsan sagittis aliquet. Pellentesque imperdiet libero vitae pellentesque cursus. Ut sollicitudin pharetra nunc, vitae pellentesque ex ultrices ac. Pellentesque ac iaculis ante, nec ultrices ex. Nunc commodo tincidunt nibh sit amet faucibus. Vestibulum fringilla, erat a dictum interdum, risus felis molestie urna, in ornare metus ligula ut magna.\n" +
            "\n" +
            "Nulla a bibendum mauris. Sed lectus nibh, laoreet nec mattis in, ullamcorper nec erat. Suspendisse eleifend sapien id dolor faucibus, convallis vulputate tortor bibendum. Etiam auctor eros vel erat laoreet, sit amet aliquam arcu aliquet. Morbi eu purus egestas, mollis dui quis, malesuada tellus. Pellentesque dignissim, ligula a maximus maximus, velit sem placerat purus, at vehicula ante lectus a velit. Curabitur eu est in metus tempus consequat. Donec diam ligula, accumsan sit amet tempor rutrum, consequat at magna. Pellentesque interdum velit eget aliquam egestas.\n" +
            "\n" +
            "Nulla venenatis lectus velit, vel ultrices mi accumsan ac. Maecenas sodales ante vel velit laoreet, a interdum tellus convallis. Morbi mollis est erat, ut venenatis neque condimentum at. Proin aliquam congue nulla eu pulvinar. Fusce leo ligula, accumsan eget porttitor sit amet, auctor interdum nisi. Nam vel ligula at felis mollis fermentum. Duis libero libero, tempus ac mauris efficitur, rutrum ultrices magna. Ut consectetur maximus sapien a faucibus. Sed mi turpis, porttitor at auctor a, tempus non diam. Proin est turpis, consequat eu gravida ut, rutrum sed nisl. Nunc non mi eu magna dapibus tempor ut sed lacus.\n" +
            "\n" +
            "Integer non nunc nisi. Fusce pharetra dolor augue, blandit posuere risus scelerisque quis. Duis tempus quis libero et convallis. Nam in sem nisi. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Fusce cursus urna vel lorem sagittis, luctus malesuada metus dapibus.\n" +
            "\n" +
            "Donec rutrum, enim sagittis consectetur gravida, odio ex commodo urna, ut commodo ipsum diam id turpis. Praesent interdum fringilla tortor, id consequat diam laoreet elementum. Donec tempus sem lorem, nec gravida elit ultrices quis. Nunc rutrum, lectus vitae dictum egestas, risus justo tempor ante, ut euismod orci velit sed ex. Nunc condimentum, mauris nec laoreet gravida, ex risus feugiat ipsum, eu hendrerit felis erat sagittis magna. Donec suscipit turpis ut ligula sodales, ut viverra massa pretium. Quisque pulvinar congue tortor, eu placerat justo. Maecenas a tellus in sem ultrices sagittis. Nullam luctus semper elit, non pellentesque dui fringilla ac. Donec vestibulum vehicula orci, in placerat ligula gravida a.\n" +
            "\n" +
            "Vivamus condimentum arcu elit, facilisis congue libero egestas fringilla. Ut ac pharetra ligula. Quisque suscipit velit at erat placerat, vitae rutrum enim sodales. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus in bibendum orci. Donec elementum sodales tincidunt. Suspendisse sit amet sapien at mauris interdum efficitur. Maecenas eget magna nulla.\n" +
            "\n" +
            "Nunc euismod, felis volutpat ultrices bibendum, ex elit mattis urna, ut porttitor lectus massa quis nulla. Quisque lacinia auctor ipsum vel faucibus. Mauris non sem fringilla, tincidunt risus et, maximus lacus. Fusce facilisis quis felis sit amet aliquet. Maecenas a molestie quam. Vestibulum a libero quis diam dapibus faucibus. Nunc placerat lobortis ligula, rhoncus sagittis eros facilisis sit amet. In scelerisque massa ut felis sagittis, vulputate iaculis tortor aliquam.\n" +
            "\n" +
            "Mauris tincidunt tellus vel nisi vestibulum pretium. Suspendisse hendrerit faucibus purus at pharetra. Sed elementum ligula at mauris vestibulum mattis. Fusce malesuada tincidunt odio convallis pretium. Nam tincidunt, nisi sit amet pellentesque rhoncus, velit purus malesuada elit, ac egestas nunc diam quis ipsum. Phasellus nunc lectus, accumsan vitae tellus iaculis, porta efficitur ligula. Nunc nec mollis est. Mauris vel nisl tempor, bibendum sem ac, vestibulum tellus. Nulla et libero gravida justo efficitur condimentum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Cras sit amet blandit mi, molestie pellentesque ipsum. Vivamus ultrices accumsan turpis sed posuere. In molestie massa sed sem finibus interdum. Pellentesque aliquam blandit dapibus. Phasellus dapibus odio sem, a mollis enim fermentum eget.\n" +
            "\n" +
            "Aliquam lobortis lorem dolor, sit amet euismod erat vestibulum sit amet. Fusce semper faucibus sem ac dictum. Aliquam erat volutpat. Fusce tellus nisi, convallis ac mi eu, dictum posuere diam. Suspendisse viverra aliquet urna. Etiam commodo metus ac ligula consequat imperdiet. Nam sem dui, porta sed elit eget, finibus faucibus neque. Integer tempor urna euismod, malesuada sem in, hendrerit eros. Maecenas faucibus diam metus, et ultrices erat finibus eget. Maecenas sagittis nec nisi id imperdiet. Mauris vitae mollis lorem.\n" +
            "\n" +
            "Aliquam rhoncus nec ante sed fringilla. Suspendisse nec erat placerat, pellentesque urna nec, interdum magna. Sed accumsan ut libero nec vulputate. Nullam ultrices semper justo, ut euismod diam pellentesque in. Curabitur vitae nisl ac purus facilisis tempor eu et tellus. Morbi ultricies, diam a euismod volutpat, lacus massa lacinia nisi, at iaculis metus mi eget mauris. Nullam ut est vel elit congue mollis ut vel leo. Sed ultrices nulla eget velit tempus rhoncus. Nam faucibus finibus sapien. Integer vestibulum venenatis augue, eu euismod nisi fringilla eget. Nulla viverra sapien id efficitur consequat. Suspendisse pulvinar euismod consequat. Sed maximus enim non justo vestibulum egestas. Nunc porta eu sapien ac tincidunt.\n" +
            "\n" +
            "Proin nec elit placerat, dictum justo non, blandit ante. In hac habitasse platea dictumst. Nam pellentesque augue vitae nisi tempus posuere vel ut mi. Curabitur aliquam venenatis pretium. Nullam aliquet tellus non ornare molestie. Aenean rhoncus tristique nulla at interdum. Nam pellentesque tortor purus, eget pretium libero malesuada in. Morbi at quam risus. Nam semper, mauris interdum mattis tempus, mi massa pulvinar libero, eu lobortis quam felis ut nisi. Cras rhoncus, sem in cursus bibendum, dui diam convallis enim, vel porttitor ligula elit non arcu.\n" +
            "\n" +
            "Nulla eget leo ipsum. In vel leo pellentesque, tristique tortor sit amet, rutrum leo. Nulla odio odio, feugiat id congue sit amet, tincidunt cursus augue. Phasellus fringilla nulla nisi.";


    public DescriptionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        TextView textView = view.findViewById(R.id.descriptionTextView);
        textView.setText(LOREM);
        return view;
    }
}